package bus.rmi;

import java.awt.AWTException;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServerRMI {
    private boolean is_binding;
    private String url;

    
    public ServerRMI() {
        this.url = null;
        this.is_binding = false;
    }

    public void startBindingOnRmiServer(String host, int port)
            throws RemoteException, MalformedURLException, AWTException {
        if (!this.is_binding) {
            try {
                this.url = "rmi://" + host + ":" + port + "/RemoteDesktop";
                System.setProperty("java.rmi.server.hostname", host);
                LocateRegistry.createRegistry(port);
                Naming.rebind(this.url, new RemoteDesktopImplementation());
                this.is_binding = true;
            } catch (Exception e) {
                Naming.rebind(this.url, new RemoteDesktopImplementation());
            }
        }
    }

    public void stopBinding() throws NotBoundException, AccessException, RemoteException, MalformedURLException {
        if (this.is_binding) {
            Naming.unbind(this.url);
            this.url = null;
            this.is_binding = false;
        }
    }

    public boolean getIsBinding() {
        return this.is_binding;
    }
}
