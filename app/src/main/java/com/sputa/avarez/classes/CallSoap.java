package com.sputa.avarez.classes;
import com.sputa.avarez.Functions;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.UUID;

import static com.sputa.avarez.Functions.Lag;

public class CallSoap
{
    String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

    String SOAP_ADDRESS_Nosazi_Council = "http://testkasbapp.urmia.ir/myservice.asmx";
    String SOAP_ADDRESS_Nosazi_Mine = "http://app.e-paytoll.ir/nosazi.asmx";
    String SOAP_ADDRESS_Bussiness_Mine = "http://app.e-paytoll.ir/Bussines.asmx";

    public CallSoap()
    {
    }

    public String Call_Nosazi_GetInfo(String PnosaziKodem,UUID AID,String Atuh)
    {
        String SOAP_ACTION = "http://tempuri.org/GetNosaziInfo";

        String OPERATION_NAME = "GetNosaziInfo";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        request.addProperty("nosaziCode",PnosaziKodem);
        request.addProperty("aid",AID.toString());
        request.addProperty("atuh",Atuh);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_Nosazi_Mine);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response="error-test="+exception.getMessage();
        }
        return response.toString();
    }



    public String Call_Bussiness_GetInfo(String MelliID,String Parvandeh)
    {
        String SOAP_ACTION = "http://tempuri.org/GetBussinessInfo";

        String OPERATION_NAME = "GetBussinessInfo";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        request.addProperty("melli",MelliID);
        request.addProperty("parvandeh",Parvandeh);
//        request.addProperty("Atuh",Atuh);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_Bussiness_Mine);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response="error"+exception.getMessage();
        }
        return response.toString();
    }

    public String Call_Bussiness_GetInfo_ByBussinessId(String bussinessId)
    {
        String SOAP_ACTION = "http://tempuri.org/GetBussinessInfoByBussinessId";

        String OPERATION_NAME = "GetBussinessInfoByBussinessId";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        request.addProperty("bussinessId",bussinessId);

//        request.addProperty("Atuh",Atuh);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_Bussiness_Mine);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response="error"+exception.getMessage();
        }
        return response.toString();
    }

    public String Call_Nosazi_GetAddedToList(String NosaziCode)
    {
        String SOAP_ACTION = "http://tempuri.org/GetSavedNosazi";

        String OPERATION_NAME = "GetSavedNosazi";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        request.addProperty("UserId", Functions.u_id);
        request.addProperty("NosaziCode",NosaziCode);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_Nosazi_Mine);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response="error";
        }
        return response.toString();
    }
    public String Call_Bussiness_GetAddedToList(String bussinesId)
    {
        String SOAP_ACTION = "http://tempuri.org/GetBussinessIdAdded";

        String OPERATION_NAME = "GetBussinessIdAdded";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        request.addProperty("UserId", Functions.u_id);
        request.addProperty("BussinessId",bussinesId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_Bussiness_Mine);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response="error"+exception.getMessage();
        }
        return response.toString();
    }

    public String Call_Nosazi_GetPaid(UUID AID,String Atuh)
    {
        String SOAP_ACTION = "http://tempuri.org/CheckNosazi";

        String OPERATION_NAME = "CheckNosazi";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);


        request.addProperty("aid",AID.toString());
        request.addProperty("atuh",Atuh);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_Nosazi_Mine);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response="error";
        }

        return response.toString();
    }
    public String Call_Nosazi_GetPaid(String BillId,String PaymentId,UUID AID,String Atuh)
    {
        String SOAP_ACTION = "http://tempuri.org/GetPaidNosazi";

        String OPERATION_NAME = "GetPaidNosazi";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        request.addProperty("billId",BillId);
        request.addProperty("paymentId",PaymentId);
        request.addProperty("aid",AID.toString());
        request.addProperty("atuh",Atuh);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_Nosazi_Council);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response="error";
        }
        return response.toString();
    }
    public String Call_paid(String BillId,String PaymentId,UUID AID,String Atuh)
    {
        String SOAP_ACTION = "http://tempuri.org/IsPayed";

        String OPERATION_NAME = "IsPayed";

        String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

        String SOAP_ADDRESS = "http://testkasbapp.urmia.ir/myservice.asmx";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        request.addProperty("BillId",BillId);
        request.addProperty("PaymentId",PaymentId);
        request.addProperty("AID",AID.toString());
        request.addProperty("Atuh",Atuh);



//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//                SoapEnvelope.VER11);
//        envelope.dotNet = true;
//        envelope.setOutputSoapObject(request);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    public String Call_Nosazi_SaveNosziCode(String NosaziCode) {
        String SOAP_ACTION = "http://tempuri.org/SaveNosazi";

        String OPERATION_NAME = "SaveNosazi";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        request.addProperty("UserId", Functions.u_id);
        request.addProperty("NosaziCode",NosaziCode);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_Nosazi_Mine);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response="error";
        }
        return response.toString();
    }
    public String Call_Nosazi_SaveBussinessId(String bussinessId) {
        String SOAP_ACTION = "http://tempuri.org/SaveBussiness";

        String OPERATION_NAME = "SaveBussiness";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        request.addProperty("UserId", Functions.u_id);
        request.addProperty("BussinessId",bussinessId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_Bussiness_Mine);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response="error"+exception.getMessage();
        }
        return response.toString();
    }
    public String Call_Nosazi_DeleteBussiness(String bussinessId) {
        String SOAP_ACTION = "http://tempuri.org/DeleteBussiness";

        String OPERATION_NAME = "DeleteBussiness";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        request.addProperty("UserId", Functions.u_id);
        request.addProperty("BussinessId",bussinessId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_Bussiness_Mine);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response="error"+exception.getMessage();
        }
        return response.toString();
    }

    public String Call_Nosazi_GetNosaziList() {
        String SOAP_ACTION = "http://tempuri.org/GetNosaziList";

        String OPERATION_NAME = "GetNosaziList";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        request.addProperty("UserId", Functions.u_id);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_Nosazi_Mine);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response="error";
        }
        return response.toString();
    }
public String Call_Bussiness_GetBussinessList() {
        String SOAP_ACTION = "http://tempuri.org/GetBussinessList";

        String OPERATION_NAME = "GetBussinessList";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        request.addProperty("UserId", Functions.u_id);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_Bussiness_Mine);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response="error";
        }
        return response.toString();
    }

    public String Call_Nosazi_DeleteNosazi(String NosaziCode) {
        String SOAP_ACTION = "http://tempuri.org/DeleteNosazi";

        String OPERATION_NAME = "DeleteNosazi";


        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);

        request.addProperty("UserId", Functions.u_id);
        request.addProperty("NosaziCode", NosaziCode);
        Lag("no="+NosaziCode);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_Nosazi_Mine);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            Lag(exception.getMessage());
            response="error";

        }
        return response.toString();
    }

}