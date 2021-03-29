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

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDNonTerminalField;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.springframework.stereotype.Component;

@Component("nas5050Processor")
public class Nas5050Processor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Message msg = exchange.getMessage();
        String filename = msg.getHeader(Exchange.FILE_PARENT).toString() + "/../"
                + msg.getHeader(Exchange.FILE_NAME).toString();

        @SuppressWarnings("unchecked")
        List<Business> bizList = (List<Business>) exchange.getMessage().getBody();

        // "https://catalogue.servicecanada.gc.ca/content/EForms/en/CallForm.html?Lang=en&PDF=SC-NAS5050.pdf";
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("SC-NAS5050.pdf");
                PDDocument pdd = PDDocument.load(is)) {

            // Uncomment the following for printing out pdf form fields
            // printFields(pdd);

            pdd.setAllSecurityToBeRemoved(true);
            // get the document catalog
            PDAcroForm acroForm = pdd.getDocumentCatalog().getAcroForm();

            // as there might not be an AcroForm entry a null check is necessary
            if (acroForm != null) {
                // Retrieve an individual field and set its value.
                // If a field is nested within the form tree a fully qualified name
                // might be provided to access the field.
                PDRadioButton but = (PDRadioButton) acroForm
                        .getField("NAS5050_E[0].Page1[0].#subform[0].rb_Action_Requested[0]");
                but.setValue("1");

                IntStream.range(0, bizList.size()).forEach(i -> {
                    try {
                        switch (i) {
                        case 0:
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].txtF_Business_Name[0]"))
                                    .setValue(bizList.get(i).getName());
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].txtF_Contact_Person[0]"))
                                    .setValue(bizList.get(i).getContact());
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].txtF_Email[0]"))
                                    .setValue(bizList.get(i).getEmail());
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].txtF_Tele[0]"))
                                    .setValue(bizList.get(i).getPhone());
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].txtF_Add[0]"))
                                    .setValue(bizList.get(i).getAddress());
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].txtF_City[0]"))
                                    .setValue(bizList.get(i).getCity());
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].txtF_Prov[0]"))
                                    .setValue(bizList.get(i).getProvince());
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].txtF_Postal[0]"))
                                    .setValue(bizList.get(i).getPostal());
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page2[0].txtF_Sign_Date[1]"))
                                    .setValue(bizList.get(i).getSignature());
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page2[0].txtF_Sign_Date[0]"))
                                    .setValue(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].tb_1[0].tr_2[0].Cell1[0]"))
                                    .setValue(bizList.get(i).getPrjNum());
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].tb_1[0].tr_2[0].Cell2[0]"))
                                    .setValue(bizList.get(i).getPrjName());
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].tb_1[0].tr_2[0].Cell3[0]"))
                                    .setValue(bizList.get(i).getPrjLocation());
                            break;

                        case 1:
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].tb_1[0].tr_3[0].Cell1[0]"))
                                    .setValue(bizList.get(i).getPrjNum());
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].tb_1[0].tr_3[0].Cell2[0]"))
                                    .setValue(bizList.get(i).getPrjName());
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].tb_1[0].tr_3[0].Cell3[0]"))
                                    .setValue(bizList.get(i).getPrjLocation());
                            break;

                        case 2:
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].tb_1[0].tr_3[1].#field[0]"))
                                    .setValue(bizList.get(i).getPrjNum());
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].tb_1[0].tr_3[1].#field[1]"))
                                    .setValue(bizList.get(i).getPrjName());
                            ((PDTextField) acroForm.getField("NAS5050_E[0].Page1[0].tb_1[0].tr_3[1].#field[2]"))
                                    .setValue(bizList.get(i).getPrjLocation());
                            break;

                        default:
                            break;
                        }
                    } catch (IOException ie) {
                    }
                });
            }

            // Save and close the filled out form.
            pdd.save(filename);
        }
    }

    /**
     * Utility method for exploring pdf form fields
     * 
     * @param  pdfDocument
     * @throws IOException
     */
    public void printFields(PDDocument pdfDocument) throws IOException {
        PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        List<PDField> fields = acroForm.getFields();

        System.out.println(fields.size() + " top-level fields were found on the form");

        for (PDField field : fields) {
            processField(field, "|--", field.getPartialName());
        }
    }

    private void processField(PDField field, String sLevel, String sParent) throws IOException {
        String partialName = field.getPartialName();

        if (field instanceof PDNonTerminalField) {
            if (!sParent.equals(field.getPartialName()) && partialName != null) {
                sParent = sParent + "." + partialName;
            }
            System.out.println(sLevel + sParent);

            for (PDField child : ((PDNonTerminalField) field).getChildren()) {
                processField(child, "|  " + sLevel, sParent);
            }
        } else {
            String fieldValue = field.getValueAsString();
            StringBuilder outputString = new StringBuilder(sLevel);
            outputString.append(sParent);
            if (partialName != null) {
                outputString.append(".").append(partialName);
            }
            outputString.append(" = ").append(fieldValue);
            outputString.append(",  type=").append(field.getClass().getName());
            System.out.println(outputString);
        }
    }
}
