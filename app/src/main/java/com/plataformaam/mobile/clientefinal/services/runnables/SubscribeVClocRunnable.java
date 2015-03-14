package com.plataformaam.mobile.clientefinal.services.runnables;

import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;
import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUserRole;
import com.plataformaam.mobile.clientefinal.services.VCLocClientService;


/**
 * Created by bernard on 13/02/2015.
 */
public class SubscribeVClocRunnable implements Runnable {

    VCLocClientService mService;
    VComComposite composite;
    VComUserRole userRole;

    public SubscribeVClocRunnable(VCLocClientService service, VComComposite composite,VComUserRole userRole) {
        this.mService = service;
        this.composite = composite;
    }

    @Override
    public void run() {

    }






}
