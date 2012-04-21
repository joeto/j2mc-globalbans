package to.joe.j2mc.globalbans.mcbouncer;

public class LookupResult {
    public boolean success;
    public String error;
    public int totalcount;
    public int page;
    public Ban[] data;
    public class Ban{
        public String username;
        public String issuer;
        public String server;
        public String reason;
        public String time;
    }
}
