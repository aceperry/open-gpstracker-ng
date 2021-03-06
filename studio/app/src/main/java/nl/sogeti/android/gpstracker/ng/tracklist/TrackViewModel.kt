/*------------------------------------------------------------------------------
 **     Ident: Sogeti Smart Mobile Solutions
 **    Author: rene
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
package nl.sogeti.android.gpstracker.ng.tracklist

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import nl.sogeti.android.gpstracker.v2.R

class TrackViewModel(uri: Uri) {

    constructor(uri: Uri, name: String) : this(uri) {
        this.name.set(name)
    }

    val uri = ObservableField<Uri>(uri)
    val name = ObservableField<String>()
    val iconType = ObservableInt(R.drawable.ic_track_type_default)
    val startDay = ObservableField<String>("--")
    val duration = ObservableField<String>("--")
    val distance = ObservableField<String>("--")
    val completeBounds = ObservableField<LatLngBounds?>()
    val waypoints = ObservableField<List<List<LatLng>>>(listOf())
    var polylines = listOf<PolylineOptions>()
}