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
package nl.sogeti.android.gpstracker.ng.utils

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.util.*

class DefaultResultHandler : ResultHandler {
    var name: String? = null
    val waypoints = mutableListOf<MutableList<LatLng>>()
    var boundsBuilder: LatLngBounds.Builder? = null
    var headBuilder: LatLngBounds.Builder? = null
    val bound: LatLngBounds
        get() {
            val builder = boundsBuilder
            val bounds: LatLngBounds
            if (builder != null) {
                bounds = builder.build()
            } else {
                bounds = LatLngBounds(LatLng(0.0, 0.0), LatLng(0.0, 0.0))
            }
            return bounds
        }

    val FIVE_MINUTES_IN_MS = 5L * 60L * 1000L
    private val headTime: Long

    init {
        headTime = System.currentTimeMillis() - FIVE_MINUTES_IN_MS
    }

    override fun addTrack(name: String) {
        this.name = name

    }

    override fun addSegment() {
        waypoints.add(ArrayList<LatLng>())
    }

    override fun addWaypoint(latLng: LatLng, millisecondsTime: Long) {
        // Last 5 minutes worth of waypoints make the head
        if (millisecondsTime > headTime) {
            headBuilder = headBuilder ?: LatLngBounds.Builder()
            headBuilder?.include(latLng)
        }
        // Add each waypoint to the end of the last list of points (the current segment)
        waypoints[waypoints.size - 1].add(latLng)
        // Build a bounds for the whole track
        boundsBuilder = boundsBuilder ?: LatLngBounds.Builder()
        boundsBuilder?.include(latLng)
    }


}