package com.example.julien.geoapp.SubPos;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;

/**
 * Solves a Trilateration problem with an instance of a
 * {@link LeastSquaresOptimizer}
 *
 * The MIT License (MIT)
 * Copyright (c) 2014 Scott Wiedemann
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
public class Position {
    double      lat;
    double      lng;
    double      altitude;
    double      distance;
    double      errorRadius;
    boolean     errorCalced;
    Position (double x, double y)
    {
        lat = x;
        lng = y;
    }

    Position (double x, double y, double z)
    {
        lat = x;
        lng = y;
        altitude = z;
    }

    Position (double x, double y, double z, double d)
    {
        lat = x;
        lng = y;
        altitude = z;
        distance = d;

    }

    Position (double x, double y, double z, double error, boolean errorCalc)
    {
        lat = x;
        lng = y;
        altitude = z;
        errorRadius = error;
        errorCalced = errorCalc;

    }


    @Override
    public String toString()
    {
        String data = new String();
        data = data + ((double)((long)(this.lat * 10000000)))/10000000 + ", ";
        data = data + ((double)((long)(this.lng * 10000000)))/10000000 + ", ";
        data = data + this.altitude / 100; //Convert to meters
        if (errorCalced) {
            data = data + ", " + ((double)((long)(this.errorRadius * 10000000)))/10000000;
        }

        return data;
    }
}
