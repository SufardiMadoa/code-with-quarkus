package org.apptest.response;

public class RsvpStatusResponse {
    public long hadir;
    public long tidakHadir;
    public long total;

    public RsvpStatusResponse(long hadir, long tidakHadir) {
        this.hadir = hadir;
        this.tidakHadir = tidakHadir;
        this.total = hadir + tidakHadir;
    }
}
