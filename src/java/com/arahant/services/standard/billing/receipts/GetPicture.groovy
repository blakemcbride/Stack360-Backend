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

package com.arahant.services.standard.billing.receipts

import com.arahant.business.BExpenseReceipt
import com.arahant.reports.TiffReport
import com.arahant.servlets.REST
import com.arahant.utils.HibernateSessionUtil
import org.json.JSONObject

/**
 * Author: Blake McBride
 * Date: 4/14/18
 */
class GetPicture {

    static void main(JSONObject injson, JSONObject outjson, HibernateSessionUtil hsu, REST service) {
        BExpenseReceipt ber = new BExpenseReceipt(injson.getString("expense_receipt_id"))
        byte [] pic = ber.getPicture1()
        String fname = "Receipt-" + ber.getPerson().getLname() + '-' + ber.getReceiptDate() + '-' + (new Date()).getTime()


        String rfname = new TiffReport().tifToPdf(pic, ber.getFileType(), fname)
        outjson.put("url", '/' + rfname)
    }

}
