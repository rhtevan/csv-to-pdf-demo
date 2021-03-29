/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.fuse.demo;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy;
import org.springframework.stereotype.Component;

@Component
public class Routes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Generate some business objects with random data
        from("timer:generateBiz?period={{timer.period}}&delay={{timer.delay}}")
                .log("Generating randomized businesses CSV data")
                .process("businessGenerator")
                // Marshal each business to CSV format
                .marshal().bindy(BindyType.Csv, Business.class)
                // Write CSV data to file
                .setHeader(Exchange.FILE_NAME, simple("projects-${exchangeId}.csv"))
                .to("file:{{doc.location}}/csv");

        // Consume business CSV files
        from("file:{{doc.location}}/csv?delay=1000&noop=true")
                .log("Reading business CSV data from ${header.CamelFileName}")
                .unmarshal().bindy(BindyType.Csv, Business.class)
                .split(body())
                .to("direct:aggregateBiz");

        // Aggregate businesses based on their stock ticker 
        from("direct:aggregateBiz")
                .setHeader("biz", simple("${body.ticker}"))
                .aggregate(simple("${body.ticker}"), new GroupedBodyAggregationStrategy())
                .completionSize(3)
                .completionInterval(5000)
                .log("Processed ${header.CamelAggregatedSize} projects for business '${header.biz}'")
                .to("seda:processed");

        // asynchronous processing
        from("seda:processed")
                .setHeader(Exchange.FILE_NAME, simple("SC-NAS5050-${header.biz}-${exchangeId}.pdf"))
                .process("nas5050Processor")
                .log("Created ${header.CamelFileName}");

        // upload generated pdf files
        // IMPORTANT: To avoid corrupted pdf, MUST set binary to true
        // from("file:{{doc.location}}?delay=1000&noop=true&includeExt=pdf")
        //         .to("ftp://{{ftp.username}}@{{ftp.host}}:{{ftp.port}}/htdocs/pdf?password={{ftp.password}}&binary=true")
        //         .log("Uploaded ${header.CamelFileName}");
    }
}
