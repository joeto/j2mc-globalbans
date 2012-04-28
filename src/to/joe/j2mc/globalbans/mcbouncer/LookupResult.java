package to.joe.j2mc.globalbans.mcbouncer;

public class LookupResult {
    public class Ban {
        public String username;
        public String issuer;
        public String server;
        public String reason;
        public String time;
    }
    public boolean success;
    public String error;
    public int totalcount;
    public int page;

    public Ban[] data;
}
