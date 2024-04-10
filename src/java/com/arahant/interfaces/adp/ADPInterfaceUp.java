/*
    STACK360 - Web-based Business Management System
    Copyright (C) 2024 Arahant LLC

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses.
*/

package com.arahant.interfaces.adp;

import com.arahant.utils.ArahantLogger;
import com.arahant.utils.FileSystemUtils;
import org.apache.log4j.Level;
import org.json.JSONObject;
import org.kissweb.RestClient;

import java.io.File;

/**
 * Author: Blake McBride
 * Date: 10/29/21
 */
public class ADPInterfaceUp {

    private static final ArahantLogger logger = new ArahantLogger(ADPInterfaceUp.class);
    private static final boolean debugging = false;

    public static void main(String [] args) throws Exception {
        String accessToken = GetAccessToken.getAccessToken();
        System.out.println("Access token = " + accessToken);
        if (Globals.access_token == null) {
            System.out.println("Aborting interface due to null access_token");
            return;
        }
        JSONObject jo = new JSONObject("{\n" +
                "  \"applicantOnboarding\": {\n" +
                "    \"onboardingTemplateCode\": {\n" +
                "      \"code\": \"169748818049_1\",\n" +
                "      \"name\": \"\"\n" +
                "    },\n" +
                "    \"onboardingStatus\": {\n" +
                "      \"statusCode\": {\n" +
                "        \"code\": \"complete\",\n" +
                "        \"name\": \"complete\"\n" +
                "      }\n" +
                "    },\"workerID\": {\n" +
                "      \"id\": \"\"\n" +
                "    },\n" +
                "    \"employmentEligibilityOptionCode\": {\n" +
                "      \"code\": \"0\"\n" +
                "    },\n" +
                "    \"preHireIndicator\": false,\n" +
                "    \"employmentEligibilityProfile\": {\n" +
                "      \"employerOrganization\": {\n" +
                "        \"locationNameCode\": {\n" +
                "          \"code\": \"NA\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"onboardingExperienceCode\": {\n" +
                "      \"code\": \"default\"\n" +
                "    },\n" +
                "    \"applicantWorkerProfile\": {\n" +
                "      \"selfEmployedWorkerTypeCode\": {\n" +
                "        \"code\": \"1\"\n" +
                "      },\n" +
                "      \"hireDate\": \"2020-06-12\",\n" +
                "      \"hireReasonCode\": {\n" +
                "        \"code\": \"new\",\n" +
                "        \"name\": \"MYHIRE - My Hire\"\n" +
                "      },\n" +
                "      \"workersCompensationCoverage\": {\n" +
                "        \"coverageStatus\": {\n" +
                "          \"code\": \"\"\n" +
                "        },\n" +
                "        \"coverageTypeCode\": {\n" +
                "          \"code\": \"\"\n" +
                "        },\n" +
                "        \"jobClassificationTypeCode\": {\n" +
                "          \"code\": \"\"\n" +
                "        },\n" +
                "        \"coverageClassTypeCode\": {\n" +
                "          \"code\": \"\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"reportsTo\": {\n" +
                "        \"positionID\": \"\"\n" +
                "      },\n" +
                "      \"homeWorkLocation\": {\n" +
                "        \"nameCode\": {\n" +
                "          \"code\": \"IND\",\n" +
                "          \"name\": \"\"\n" +
                "        },\n" +
                "        \"addresses\": [\n" +
                "          {\n" +
                "             \"lineOne\": \"sdasdfasdf\",\n" +
                "            \"lineTwo\": \"sdasdfasdf\",\n" +
                "            \"lineThree\": \"vfdsafaedsfsdaf\",\n" +
                "            \"cityName\": \"ca\",\n" +
                "            \"subdivisionCode\": {\n" +
                "              \"code\": \"CA\",\n" +
                "              \"name\": \"CA - California\",\n" +
                "              \"subdivisionType\": \"\"\n" +
                "            },\n" +
                "            \"countryCode\": \"US\",\n" +
                "            \"postalCode\": \"12345\",\n" +
                "            \"subdivisionCode2\": {\n" +
                "              \"code\": \"iuierwir eriueiruieu erqiweuri\",\n" +
                "              \"name\": \"\",\n" +
                "              \"subdivisionType\": \"\"\n" +
                "            }\n" +
                "          }\n" +
                "        ],\n" +
                "        \"homeshoreIndicator\": true\n" +
                "      },\n" +
                "      \"job\": {\n" +
                "        \"jobCode\": {\n" +
                "          \"code\": \"ADMIN\",\n" +
                "          \"name\": \"\"\n" +
                "        },\n" +
                "        \"wageLawCoverages\": [\n" +
                "          {\n" +
                "            \"wageLawNameCode\": {\n" +
                "              \"code\": \"\",\n" +
                "              \"name\": \"E - Exept\"\n" +
                "            }\n" +
                "          }\n" +
                "        ],\n" +
                "        \"occupationalClassifications\": [\n" +
                "          {\n" +
                "            \"classificationCode\": {\n" +
                "              \"code\": \"2\"\n" +
                "            },\n" +
                "            \"classificationID\": {\n" +
                "              \"code\": \"EEOC\"\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"classificationCode\": {\n" +
                "              \"code\": \"EE02\"\n" +
                "            },\n" +
                "            \"classificationID\": {\n" +
                "              \"code\": \"EEO\"\n" +
                "            }\n" +
                "          }\n" +
                "        ],\n" +
                "        \"industryClassifications\": [\n" +
                "          {\n" +
                "            \"classificationCode\": {\n" +
                "              \"code\": \"2352\",\n" +
                "              \"name\": \"\"\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      \"businessCommunication\": {\n" +
                "        \"landlines\": [\n" +
                "          {\n" +
                "            \"countryDialing\": \"1\",\n" +
                "            \"areaDialing\": \"541\",\n" +
                "            \"dialNumber\": \"7543017\",\n" +
                "            \"extension\": \"324\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"mobiles\": [\n" +
                "          {\n" +
                "            \"countryDialing\": \"1\",\n" +
                "            \"areaDialing\": \"541\",\n" +
                "            \"dialNumber\": \"7543016\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"pagers\": [\n" +
                "          {\n" +
                "            \"countryDialing\": \"1\",\n" +
                "            \"areaDialing\": \"541\",\n" +
                "            \"dialNumber\": \"7543017\",\n" +
                "            \"extension\": \"525\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"faxes\": [\n" +
                "          {\n" +
                "            \"countryDialing\": \"1\",\n" +
                "            \"areaDialing\": \"541\",\n" +
                "            \"dialNumber\": \"7543018\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"emails\": [\n" +
                "          {\n" +
                "            \"emailUri\": \"\",\n" +
                "            \"notificationIndicator\": false\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      \"laborUnion\": {\n" +
                "        \"laborUnionCode\": {\n" +
                "          \"code\": \"L\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"bargainingUnit\": {\n" +
                "        \"bargainingUnitCode\": {\n" +
                "          \"code\": \"N\",\n" +
                "          \"name\": \"\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"officerTypeCode\": {\n" +
                "        \"code\": \"EMP\",\n" +
                "        \"name\": \"\"\n" +
                "      },\n" +
                "      \"payGradeCode\": {\n" +
                "        \"code\": \"GRAD1\",\n" +
                "        \"name\": \"\"\n" +
                "      },\n" +
                "      \"benefitsEligibilityClassCode\": {\n" +
                "        \"code\": \"A\",\n" +
                "        \"name\": \"\"\n" +
                "      },\n" +
                "      \"managementPositionIndicator\": true,\n" +
                "      \"workerTypeCode\": {\n" +
                "        \"code\": \"f\",\n" +
                "        \"name\": \"\"\n" +
                "      },\n" +
                "      \"acaBenefitEligibilityCode\": {\n" +
                "        \"code\": \"D\",\n" +
                "        \"name\": \"D - Designate Full Time\"\n" +
                "      },\n" +
                "      \"acaBenefitEligibilityDate\": \"2019-11-18\",\n" +
                "      \"homeOrganizationalUnits\": [\n" +
                "        {\n" +
                "          \"unitTypeCode\": {\n" +
                "            \"code\": \"BusinessUnit\",\n" +
                "            \"name\": \"\"\n" +
                "          },\n" +
                "          \"nameCode\": {\n" +
                "            \"code\": \"\",\n" +
                "            \"name\": \"\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"unitTypeCode\": {\n" +
                "            \"code\": \"HomeDepartment\",\n" +
                "            \"name\": \"\"\n" +
                "          },\n" +
                "          \"nameCode\": {\n" +
                "            \"code\": \"\",\n" +
                "            \"name\": \"\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"unitTypeCode\": {\n" +
                "            \"code\": \"HomeCostNumber\",\n" +
                "            \"name\": \"\"\n" +
                "          },\n" +
                "          \"nameCode\": {\n" +
                "            \"code\": \"\",\n" +
                "            \"name\": \"\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"applicantPayrollProfile\": {\n" +
                "              \"payrollFileNumber\": \"\",\n" +
                "              \"overtimeEligibilityIndicator\": true,\n" +
                "      \"payrollGroupCode\": \"938\",\n" +
                "  \"standardHours\": {\n" +
                "    \"hoursQuantity\": \"\",\n" +
                "   \n" +
                "    \"unitCode\": {\n" +
                "      \"code\": \"H\"\n" +
                "    }\n" +
                "  },\n" +
                "      \"tippedWorkerIndicator\": true,\n" +
                "      \"baseRemuneration\": {\n" +
                "             \n" +
                "        \"recordingBasisCode\": {\n" +
                "          \"code\": \"1\"\n" +
                "        },\n" +
                "              \"payPeriodRateAmount\": {\n" +
                "                    \"amount\": \"100\"\n" +
                "                }\n" +
                "      },              \n" +
                " \n" +
                " \n" +
                "      \"additionalRemunerations\": [\n" +
                "        {\n" +
                "          \"remunerationTypeCode\": {\n" +
                "            \"code\": \"Rate2Amount\"\n" +
                "          },\n" +
                "          \"remunerationRate\": {\n" +
                "            \"rate\": 545\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"remunerationTypeCode\": {\n" +
                "            \"code\": \"Rate3Amount\"\n" +
                "          },\n" +
                "          \"remunerationRate\": {\n" +
                "            \"rate\": 30\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"remunerationTypeCode\": {\n" +
                "            \"code\": \"Rate4Amount\"\n" +
                "          },\n" +
                "          \"remunerationRate\": {\n" +
                "            \"rate\": 40\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"remunerationTypeCode\": {\n" +
                "            \"code\": \"Rate5Amount\"\n" +
                "          },\n" +
                "          \"remunerationRate\": {\n" +
                "            \"rate\": 50\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"remunerationTypeCode\": {\n" +
                "            \"code\": \"Rate6Amount\"\n" +
                "          },\n" +
                "          \"remunerationRate\": {\n" +
                "            \"rate\": 60\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"remunerationTypeCode\": {\n" +
                "            \"code\": \"Rate7Amount\"\n" +
                "          },\n" +
                "          \"remunerationRate\": {\n" +
                "            \"rate\": 70\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"remunerationTypeCode\": {\n" +
                "            \"code\": \"Rate8Amount\"\n" +
                "          },\n" +
                "          \"remunerationRate\": {\n" +
                "            \"rate\": 80\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"remunerationTypeCode\": {\n" +
                "            \"code\": \"Rate9Amount\"\n" +
                "          },\n" +
                "          \"remunerationRate\": {\n" +
                "            \"rate\": 90\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"wageEntityCode\": {\n" +
                "        \"code\": \"AZ - FLAGST - FLAGSTAFF\"\n" +
                "      },\n" +
                " \n" +
                "      \"customFieldGroup\": {\n" +
                "        \"stringFields\": [\n" +
                "          {\n" +
                "            \"nameCode\": {\n" +
                "              \"code\": \"CustomArea1\"\n" +
                "            },\n" +
                "            \"stringValue\": \"dsfds\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"nameCode\": {\n" +
                "              \"code\": \"CustomArea2\"\n" +
                "            },\n" +
                "            \"stringValue\": \"dfgdgjhklm\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"nameCode\": {\n" +
                "              \"code\": \"CustomArea3\"\n" +
                "            },\n" +
                "            \"stringValue\": \"dfgdgjhklm\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"nameCode\": {\n" +
                "              \"code\": \"CustomArea4\"\n" +
                "            },\n" +
                "            \"stringValue\": \"jadfdfds\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"codeFields\": [\n" +
                "          {\n" +
                "            \"nameCode\": {\n" +
                "              \"code\": \"\"\n" +
                "            },\n" +
                "            \"code\": \"\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"nameCode\": {\n" +
                "              \"code\": \"\"\n" +
                "            },\n" +
                "            \"code\": \"\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    \"applicantPersonalProfile\": {\n" +
                "      \"birthName\": {\n" +
                "        \"givenName\": \"fname01\",\n" +
                "        \"middleName\": \"mn\",\n" +
                "        \"familyName\": \"lname01\"\n" +
                "      },\n" +
                "      \"legalName\": {\n" +
                "        \"salutations\": [\n" +
                "          {\n" +
                "            \"code\": \"Hon.\",\n" +
                "            \"name\": \"Hon. - Hon.\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"generationAffix\": {\n" +
                "          \"code\": \"I\",\n" +
                "          \"name\": \"I - the first\"\n" +
                "        },\n" +
                "        \"qualificationAffix\": {\n" +
                "          \"code\": \"RN\",\n" +
                "          \"name\": \"RN - Registered Nurse\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"preferredName\": {\n" +
                "        \"nickName\": \"venu\"\n" +
                "      },\n" +
                "      \"genderCode\": {\n" +
                "        \"code\": \"M\",\n" +
                "        \"name\": \"M - Male\"\n" +
                "      },\n" +
                "      \"birthDate\": \"1980-01-01\",\n" +
                "      \"governmentIDs\": [\n" +
                "        {\n" +
                "          \"id\": \"\",\n" +
                "          \"nameCode\": {\n" +
                "            \"code\": \"SSN\",\n" +
                "            \"name\": \"SSN - Social Security Number\"\n" +
                "          },\n" +
                "          \"statusCode\": {\n" +
                "            \"code\": \"AppliedFor\",\n" +
                "            \"name\": \"\",\n" +
                "            \"effectiveDateTime\": \"\"\n" +
                "          },\n" +
                "          \"expirationDate\": \"\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"legalAddress\": {\n" +
                "        \"lineOne\": \"1372 Broadway dsafdsf\",\n" +
                "        \"lineTwo\": \"dfadfdasfsa\",\n" +
                "        \"lineThree\": \"dfadfdasfsa\",\n" +
                "        \"cityName\": \"fg fg fgdf\",\n" +
                "        \"subdivisionCode\": {\n" +
                "          \"code\": \"NY\",\n" +
                "          \"name\": \"NY - New York\",\n" +
                "          \"subdivisionType\": \"\"\n" +
                "        },\n" +
                "        \"countryCode\": \"US\",\n" +
                "        \"postalCode\": \"10018\",\n" +
                "        \"subdivisionCode2\": {\n" +
                "          \"code\": \"sddfdjflkajsdfoiasdj\",\n" +
                "          \"name\": \"\",\n" +
                "          \"subdivisionType\": \"\"\n" +
                "        },\n" +
                "        \"deliveryPoint\": \"work mail stop\"\n" +
                "      },\n" +
                "      \"otherPersonalAddresses\": [\n" +
                "        {\n" +
                "          \"lineOne\": \"fdsgf\",\n" +
                "          \"lineTwo\": \"\",\n" +
                "          \"lineThree\": \"\",\n" +
                "          \"cityName\": \"dfasdfasd\",\n" +
                "          \"subdivisionCode\": {\n" +
                "            \"code\": \"PA\",\n" +
                "            \"name\": \"PA - Pennsylvania\",\n" +
                "            \"subdivisionType\": \"\"\n" +
                "          },\n" +
                "          \"countryCode\": \"US\",\n" +
                "          \"postalCode\": \"10018\",\n" +
                "          \"subdivisionCode2\": {\n" +
                "            \"code\": \"Philadelphia County\",\n" +
                "            \"name\": \"\",\n" +
                "            \"subdivisionType\": \"\"\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"languageCode\": {\n" +
                "        \"code\": \"en_US\",\n" +
                "        \"name\": \"en_US - English (US)\"\n" +
                "      },\n" +
                "      \"maritalStatusCode\": {\n" +
                "        \"code\": \"M\",\n" +
                "        \"name\": \"M - Married\",\n" +
                "        \"effectiveDateTime\": \"1980-06-06\"\n" +
                "      },\n" +
                "      \"tobaccoUserIndicator\": true,\n" +
                "      \"socialInsurancePrograms\": [\n" +
                "        {\n" +
                "          \"nameCode\": {\n" +
                "            \"code\": \"Medicare\",\n" +
                "            \"name\": \"Medicare\"\n" +
                "          },\n" +
                "          \"coveredIndicator\": true\n" +
                "        },\n" +
                "        {\n" +
                "          \"nameCode\": {\n" +
                "            \"code\": \"Medicaid\",\n" +
                "            \"name\": \"Medicaid\"\n" +
                "          },\n" +
                "          \"coveredIndicator\": true\n" +
                "        }\n" +
                "      ],\n" +
                "      \"raceCode\": {\n" +
                "        \"code\": \"1\",\n" +
                "        \"name\": \"1 - White\",\n" +
                "        \"identificationMethodCode\": {\n" +
                "          \"code\": \"VID\",\n" +
                "          \"name\": \"VID - Visual ID\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"ethnicityCode\": {\n" +
                "        \"code\": \"4\",\n" +
                "        \"name\": \"4 - Not Hispanic or Latino\"\n" +
                "      },\n" +
                "      \"communication\": {\n" +
                "        \"landlines\": [\n" +
                "          {\n" +
                "            \"countryDialing\": \"1\",\n" +
                "            \"areaDialing\": \"541\",\n" +
                "            \"dialNumber\": \"7543011\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"mobiles\": [\n" +
                "          {\n" +
                "            \"countryDialing\": \"1\",\n" +
                "            \"areaDialing\": \"541\",\n" +
                "            \"dialNumber\": \"7543017\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"faxes\": [\n" +
                "          {\n" +
                "            \"countryDialing\": \"1\",\n" +
                "            \"areaDialing\": \"541\",\n" +
                "            \"dialNumber\": \"7543013\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"pagers\": [\n" +
                "          {\n" +
                "            \"countryDialing\": \"1\",\n" +
                "            \"areaDialing\": \"541\",\n" +
                "            \"dialNumber\": \"7543017\",\n" +
                "            \"extension\": \"013\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"emails\": [\n" +
                "          {\n" +
                "            \"emailUri\": \"venugopal.doddy@adp.com\",\n" +
                "            \"notificationIndicator\": true\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      \"disabledIndicator\": true,\n" +
                "      \"disabilityTypeCodes\": [\n" +
                "        {\n" +
                "          \"code\": \"D\",\n" +
                "          \"name\": \"D - I don't wish to answer\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },       \n" +
                "    \"applicantTaxProfile\": {\n" +
                "      \"usFederalTaxInstruction\": {\n" +
                "               \"multipleJobIndicator\": true,\n" +
                "        \"federalIncomeTaxInstruction\": {\n" +
                "        \n" +
                "          \"taxFilingStatusCode\": {\n" +
                "            \"code\": \"D\"\n" +
                "          },\n" +
                "          \"taxWithholdingStatus\": {\n" +
                "            \"statusCode\": {\n" +
                "              \"code\": \"\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"federalUnemploymentTaxInstruction\": {\n" +
                "            \"taxWithholdingStatus\": {\n" +
                "              \"statusCode\": {\n" +
                "                \"code\": \"1\"\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"taxAllowanceQuantity\": 20,\n" +
                "          \"additionalIncomeAmount\": {\n" +
                "            \"amount\": 10\n" +
                "          },\n" +
                "          \"additionalTaxAmount\":    {\n" +
                "            \"amount\": 99999\n" +
                "          },\n" +
                "          \"taxAllowances\": [\n" +
                "            {\n" +
                "              \"allowanceTypeCode\": {\n" +
                "                \"code\": \"Deductions\"\n" +
                "              },\n" +
                "              \"taxAllowanceAmount\": {\n" +
                "                \"amount\": 13\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"allowanceTypeCode\": {\n" +
                "                \"code\": \"Dependents\"\n" +
                "              },\n" +
                "              \"taxAllowanceAmount\": {\n" +
                "                \"amount\": 12\n" +
                "              }\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      },\n" +
                "      \"usStateTaxInstructions\": {\n" +
                "        \"stateIncomeTaxInstructions\": [\n" +
                "          {\n" +
                "            \"workedInJurisdictionIndicator\": true,\n" +
                "            \"stateCode\": {\n" +
                "              \"code\": \"OH\"\n" +
                "            },\n" +
                "            \"taxFilingStatusCode\": {\n" +
                "              \"code\": \"S\"\n" +
                "            },\n" +
                "            \"taxAllowanceQuantity\": 20,\n" +
                "            \"additionalTaxAmount\": {\n" +
                "              \"amount\": 99\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"livedInJurisdictionIndicator\": true,\n" +
                "            \"stateCode\": {\n" +
                "              \"code\": \"OH\"\n" +
                "            }\n" +
                "          }\n" +
                "        ],\n" +
                "        \"suiTaxInstruction\": {\n" +
                "          \"stateCode\": {\n" +
                "            \"code\": \"19\"\n" +
                "          },\n" +
                "          \"healthCoverageCode\": {\n" +
                "            \"code\": \"4\"\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      \"localTaxInstructions\": [\n" +
                "        {\n" +
                "          \"livedInJurisdictionIndicator\": true,\n" +
                "          \"localCode\": {\n" +
                "            \"code\": \"040Y\"\n" +
                "          },\n" +
                "          \"taxAllowanceQuantity\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"workedInJurisdictionIndicator\": true,\n" +
                "          \"localCode\": {\n" +
                "            \"code\": \"040Y\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"taxTypeCode\": {\n" +
                "            \"code\": \"\"\n" +
                "          },\n" +
                "          \"localCode\": {\n" +
                "            \"code\": \"\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"taxTypeCode\": {\n" +
                "            \"code\": \"PA_LST_Local_4\"\n" +
                "          },\n" +
                "          \"localCode\": {\n" +
                "            \"code\": \"\"\n" +
                "          }\n" +
                "        },\n" +
                "                {\n" +
                "          \"taxTypeCode\": {\n" +
                "            \"code\": \"PA_LST_Local_5\"\n" +
                "          },\n" +
                "          \"localCode\": {\n" +
                "            \"code\": \"\"\n" +
                "          }\n" +
                "        },\n" +
                " \n" +
                "        {\n" +
                "          \"taxTypeCode\": {\n" +
                "            \"code\": \"NY_MTA_Local\"\n" +
                "          },\n" +
                "          \"localCode\": {\n" +
                "            \"code\": \"\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                " \n" +
                "  }\n" +
                "}");

        String urlString;
        RestClient rc = new RestClient();
        final String serviceURL = "https://api.adp.com/hcm/v2/applicant.onboard";

        if (debugging) {
            logger.setLevel(Level.ALL);
            rc.setDebugFileName("ADPInterfaceDebugLog.txt");
        }

        if (serviceURL.contains("?"))
            urlString = serviceURL + "&preventCache=" + System.currentTimeMillis();
        else
            urlString = serviceURL + "?preventCache=" + System.currentTimeMillis();

        JSONObject headers = new JSONObject();
        headers.put("Connection", "close");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept", "application/json;masked=false");
        if (Globals.access_token != null)
            headers.put("Authorization", "Bearer " + Globals.access_token);

        if (Globals.proxyServerURL != null  &&  !Globals.proxyServerURL.isEmpty())
            rc.setProxy(Globals.proxyServerURL, Globals.proxyServerPort);

        File workingDir = FileSystemUtils.getWorkingDirectory();
        File pKeyFile = new File(workingDir, "WEB-INF/keys/ADP/" + Globals.CLIENT_KEYSTORE_PATH);
        rc.setTLSKey(Globals.CLIENT_KEYSTORE_TYPE, pKeyFile.getAbsolutePath(), Globals.CLIENT_KEYSTORE_PASS);

        JSONObject ret = rc.jsonCall("POST", urlString, jo, headers);
        int respCode = rc.getResponseCode();
        String respString = rc.getResponseString();
        int x = 1;
    }
}
