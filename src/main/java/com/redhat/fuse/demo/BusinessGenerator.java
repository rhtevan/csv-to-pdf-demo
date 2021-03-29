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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

/**
 * Processor to generate {@Business}s with a random set of data.
 */
@Component("businessGenerator")
public class BusinessGenerator implements Processor {

    private static final String[][] BIZ_INFO = {
            { "RHT", "Red Hat Canada", "John Doe", "john.doe@redhat.com", "(888) 733-4281", "90 Eglinton Ave E Suite 502",
                    "Toronto", "Ontario", "M4P 2Y3", "Laurie Krebs" },
            { "IBM", "IBM Canada", "Jane Doe", "jane.doe@ibm.com", "(800) 426-4968", "3600 Steeles Ave E", "Markham", "Ontario",
                    "L3R 9Z7", "Leanne Clarke" },
            { "MSFT", "Microsoft Canada", "John Doe", "john.doe@microsoft.com", "(905) 568-0434", "1950 Meadowvale Blvd",
                    "Mississauga", "Ontario", "L5N 8L9", "Linda Sampson" },
            { "GOOG", "Google Canada", "Jane Doe", "jane.doe@google.com", "(416) 915-8200", "111 Richmond St W", "Toronto",
                    "Ontario", "M5H 2G4", "Ruth Porat" },
            { "ORCL", "Oracle Canada", "John Doe", "john.doe@oracle.com", "(905) 890-8100", "100 Milverton Dr", "Mississauga",
                    "Ontario", "L5R 4H1", "Kevin Bone" },
    };

    private static final String[][] PRJ_INFO = {
            { "Kubernetes", "Toronto" },
            { "TensorFlow", "Edmonton" },
            { "Ansible", "Victoria" },
            { "Renovate", "Fredericton" },
            { "Django", "St. John's" },
            { "Kogito", "Halifax" },
            { "Skupper", "Charlottetown" },
            { "SmallRye", "Quebec City" },
            { "Quarkus", "Regina" },
            { "PatternFly", "Whitehorse" },
            { "VS Code", "Iqaluit" },
            { "ArgoCD", "Yellowknife" },
    };

    @Override
    public void process(Exchange exchange) throws Exception {
        Random random = new Random();
        List<Business> businesses = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            String[] bizInfo = BIZ_INFO[random.nextInt(BIZ_INFO.length)];
            String[] prjInfo = PRJ_INFO[random.nextInt(PRJ_INFO.length)];

            Business biz = new Business();
            biz.setId(i);

            biz.setTicker(bizInfo[0]);
            biz.setName(bizInfo[1]);
            biz.setContact(bizInfo[2]);
            biz.setEmail(bizInfo[3]);
            biz.setPhone(bizInfo[4]);
            biz.setAddress(bizInfo[5]);
            biz.setCity(bizInfo[6]);
            biz.setProvince(bizInfo[7]);
            biz.setPostal(bizInfo[8]);
            biz.setSignature(bizInfo[9]);

            biz.setPrjNum(String.valueOf(random.nextInt(100)));
            biz.setPrjName(prjInfo[0]);
            biz.setPrjLocation(prjInfo[1]);

            businesses.add(biz);
        }

        exchange.getMessage().setBody(businesses);
    }
}
