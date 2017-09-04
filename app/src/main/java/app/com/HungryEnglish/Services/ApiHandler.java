package app.com.HungryEnglish.Services;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import app.com.HungryEnglish.Util.Constant;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;


public class ApiHandler {

    public static final String BASE_URL = Constant.BASEURL;

    private static final long HTTP_TIMEOUT = TimeUnit.SECONDS.toMillis(6000);
    private static WebServices apiService;


    public static WebServices getApiService() {
        if (apiService == null) {
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(70 * 1000, TimeUnit.MILLISECONDS);
            okHttpClient.setWriteTimeout(70 * 1000, TimeUnit.MILLISECONDS);
            okHttpClient.setReadTimeout(70 * 1000, TimeUnit.MILLISECONDS);

//            okHttpClient.setSslSocketFactory(new NoSSLv3Factory());

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(BASE_URL)
                    .setClient(new OkClient(myOkHttpClient()))
                    .setConverter(new GsonConverter(new Gson()))
                    .build();


//            ConnectionSpec.Builder obsoleteSpecBuilder = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS);
//            obsoleteSpecBuilder = obsoleteSpecBuilder.cipherSuites("TLS_DHE_DSS_WITH_AES_128_CBC_SHA");
//            ConnectionSpec obsoleteSpec = obsoleteSpecBuilder.build();
//            okHttpClient.setConnectionSpecs(Arrays.asList(obsoleteSpec));

            apiService = restAdapter.create(WebServices.class);
            return apiService;
        } else {
            return apiService;
        }
    }




    protected static OkHttpClient myOkHttpClient(){

        try{
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setReadTimeout(70 * 1000, TimeUnit.MILLISECONDS);
            okHttpClient.setConnectTimeout(70 * 1000, TimeUnit.MILLISECONDS);

            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            } };


            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            okHttpClient.setSslSocketFactory(sslSocketFactory);

            return okHttpClient;

        }catch (Exception e){
            return null;
        }
    }


//    public static OkClient createClient(int readTimeout, TimeUnit readTimeoutUnit, int connectTimeout, TimeUnit connectTimeoutUnit)
//    {
//        final OkHttpClient okHttpClient = new OkHttpClient();
//        okHttpClient.setReadTimeout(readTimeout, readTimeoutUnit);
//        okHttpClient.setConnectTimeout(connectTimeout, connectTimeoutUnit);
//
//        try {
//            URL url = new URL(ApiIntentService.getHostAddress());
//            SSLSocketFactory NoSSLv3Factory = new NoSSLv3Factory(url);
//            okHttpClient.setSslSocketFactory(NoSSLv3Factory);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return new OkClient(okHttpClient);
//
//    }



}