package com.ejemplo.appdocente.Data.Remote;

import com.ejemplo.appdocente.BuildConfig;
import com.ejemplo.appdocente.Remote.DocenteService;
import com.ejemplo.appdocente.Remote.EstudianteService;
import com.ejemplo.appdocente.Remote.LocalizacionService;
import com.ejemplo.appdocente.Remote.RetrofitClient;
import com.ejemplo.appdocente.Remote.SmsService;
import com.ejemplo.appdocente.Remote.UsuarioService;

/**
 * Created by JC on 9/12/2017.
 */

public class RemoteUtils {

    private RemoteUtils(){}

    public static LocalizacionService getLocalizacionService(){
        return RetrofitClient.getClient(BuildConfig.BASE_URL).create(LocalizacionService.class);
    }

    public static SmsService getSmsService(){
        return RetrofitClient.getClient(BuildConfig.BASE_URL).create(SmsService.class);
    }

    public static UsuarioService getUsuarioService(){
        return RetrofitClient.getClient(BuildConfig.BASE_URL).create(UsuarioService.class);
    }

    public static DocenteService getDocenteService(){
        return RetrofitClient.getClient(BuildConfig.BASE_URL).create(DocenteService.class);
    }

    public static EstudianteService getEstudianteService(){
        return RetrofitClient.getClient(BuildConfig.BASE_URL).create(EstudianteService.class);
    }

}
