/*------------------------------------------------------------------------------
 **     Ident: Sogeti Smart Mobile Solutions
 **    Author: René de Groot
 ** Copyright: (c) 2016 Sogeti Nederland B.V. All Rights Reserved.
 **------------------------------------------------------------------------------
 ** Sogeti Nederland B.V.            |  No part of this file may be reproduced
 ** Distributed Software Engineering |  or transmitted in any form or by any
 ** Lange Dreef 17                   |  means, electronic or mechanical, for the
 ** 4131 NJ Vianen                   |  purpose, without the express written
 ** The Netherlands                  |  permission of the copyright holder.
 *------------------------------------------------------------------------------
 *
 *   This file is part of OpenGPSTracker.
 *
 *   OpenGPSTracker is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   OpenGPSTracker is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with OpenGPSTracker.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package nl.sogeti.android.gpstracker.ng.common.abstractpresenters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.annotation.NonNull;

import nl.sogeti.android.gpstracker.integration.ServiceConstants;
import nl.sogeti.android.gpstracker.integration.ServiceManagerInterface;
import nl.sogeti.android.gpstracker.ng.injection.Injection;
import nl.sogeti.android.gpstracker.ng.utils.TrackUriExtensionKt;

/**
 * Base class for presenters that source data from the IPC / Intent with
 * the original Open GPS Tracker app.
 */
public abstract class ConnectedServicePresenter extends ContextedPresenter {

    private static final String CONFIG_BROADCAST = Injection.CONFIG_BROADCAST;
    private static final Class serviceManagerClass = Injection.CONFIG_SERVICE;

    private ServiceManagerInterface serviceManager;

    private BroadcastReceiver receiver;
    public ConnectedServicePresenter() {
        try {
            serviceManager = (ServiceManagerInterface) serviceManagerClass.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException("Cannot start ControlPresenter without correct ServiceManagerInterface class ",e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot start ControlPresenter without correct ServiceManagerInterface class",e);
        }
    }

    @Override
    public void didStart() {
        serviceManager.startup(getContext(), new Runnable() {
            @Override
            public void run() {
                synchronized (ConnectedServicePresenter.this) {
                    Context context = ConnectedServicePresenter.this.getContext();
                    if (context != null) {
                        didConnectService(serviceManager);
                    }
                }
            }
        });
        registerReceiver();
    }

    @Override
    public void willStop() {
        unregisterReceiver();
        serviceManager.shutdown(getContext());
    }

    private void unregisterReceiver() {
        Context context = getContext();
        if (context != null && receiver != null) {
            context.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    public ServiceManagerInterface getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(ServiceManagerInterface serviceManager) {
        this.serviceManager = serviceManager;
    }

    public void setReceiver(BroadcastReceiver receiver) {
        this.receiver = receiver;
    }

    private void registerReceiver() {
        unregisterReceiver();
        receiver = new LoggerStateReceiver();
        IntentFilter filter = new IntentFilter(CONFIG_BROADCAST);
        Context context = getContext();
        if (context != null) {
            context.registerReceiver(receiver, filter);
        }
    }

    protected abstract void didConnectService(ServiceManagerInterface serviceManager);

    public abstract void didChangeLoggingState(@NonNull Uri trackUri, int loggingState);

    private class LoggerStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int loggingState = intent.getIntExtra(ServiceConstants.EXTRA_LOGGING_STATE, ServiceConstants.STATE_UNKNOWN);
            Uri trackUri = intent.getParcelableExtra(ServiceConstants.EXTRA_TRACK);
            if (trackUri == null) {
                trackUri = TrackUriExtensionKt.trackUri(-1L);
            }
            didChangeLoggingState(trackUri, loggingState);
        }
    }
}
