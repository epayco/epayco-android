package com.epayco.android;

public class Config {

 
    public static final String BASE_URL_SDK = System.getenv("BASE_URL_SDK") != null
            ? System.getenv("BASE_URL_SDK")
            : "https://eks-subscription-api-lumen-service.epayco.io";

    public static final String ENTORNO_SDK = System.getenv("ENTORNO_SDK") != null
            ? System.getenv("ENTORNO_SDK")
            : "/restpagos";

    public static final String SECURE_URL_SDK = System.getenv("SECURE_URL_SDK") != null
            ? System.getenv("SECURE_URL_SDK")
            : "https://eks-rest-pagos-service.epayco.io";

    public static final String BASE_URL_APIFY = System.getenv("BASE_URL_APIFY") != null
            ? System.getenv("BASE_URL_APIFY")
            : "https://eks-apify-service.epayco.io";



}
