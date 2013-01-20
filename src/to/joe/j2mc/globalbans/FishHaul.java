package to.joe.j2mc.globalbans;

import java.util.Map;

public class FishHaul {
    public boolean success;
    public Bans bans;

    public class Bans {
        public String username;
        Map<String, Service> services;
    }

    public class Service {
        int bans;
        Map<String, String> ban_info;
    }
}
