package arro.ex.gituserdisplayapplication.utils

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import javax.security.cert.CertificateException

/**
 * Created by Arro on 2022/1/8
 */
object OkHttpUtil {

    val TAG: String = "OkHttpUtil"

    val bodyInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor(HttpLogger()).setLevel(
        HttpLoggingInterceptor.Level.BODY)
    val headersInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor(HttpLogger()).setLevel(
        HttpLoggingInterceptor.Level.HEADERS)

    //    var client: OkHttpClient = getSafetyClient()
    var client: OkHttpClient = getUnsafetyClient()


    private fun getSafetyClient(): OkHttpClient {
        if(client == null)
            client = OkHttpClient.Builder()
                .addInterceptor(bodyInterceptor)
                .addInterceptor(headersInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

        return client
    }

    private fun getUnsafetyClient(): OkHttpClient {
        if(client == null)
            try {
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

                    }

                    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf<X509Certificate>()
                    }
                })

                val sslContext: SSLContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, null)
                val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

                var builder: OkHttpClient.Builder = OkHttpClient.Builder()

                builder.sslSocketFactory(sslSocketFactory,trustAllCerts.first() as X509TrustManager)
                builder.hostnameVerifier(HostnameVerifier { hostname, session -> true })


                client = builder
                    .addInterceptor(bodyInterceptor)
                    .addInterceptor(headersInterceptor)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()

            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        return client
    }
}

object TrustAll {
    fun socketFactory(): SSLSocketFactory? {
        var ssfFactory: SSLSocketFactory
        val sc = SSLContext.getInstance("TLS")
        sc.init(null, arrayOf<TrustManager>(trustManager()), SecureRandom())
        ssfFactory = sc.socketFactory
        return ssfFactory
    }

    class hostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String?, session: SSLSession?): Boolean {
            return true
        }
    }

    class trustManager : X509TrustManager, TrustManager {
        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkClientTrusted(x509Certificates: Array<X509Certificate>, s: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }
    }
}

class HttpLogger : HttpLoggingInterceptor.Logger {
    private val TAG = "OkHttpUtil"

    override fun log(message: String?) {
        Log.w(TAG, "message: $message")
    }
}