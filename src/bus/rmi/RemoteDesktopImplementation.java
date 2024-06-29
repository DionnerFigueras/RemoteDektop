package bus.rmi;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.imageio.ImageIO;

import gui.remote.ComputerInfo;

public class RemoteDesktopImplementation extends UnicastRemoteObject implements RemoteDesktopInterface{
    
/*     private int port;
    private String host; */

    public RemoteDesktopImplementation(/* int port, String host */) throws RemoteException {
        super();
/*         this.port = port;
        this.host = host; */
    }

    @Override
    public byte[] takeScreenshotServer(String quality) throws RemoteException {
        try {
            Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle bounds = new Rectangle(screen_size);
            Robot mr_robot = new Robot();
            BufferedImage screenshot = mr_robot.createScreenCapture(bounds);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.setUseCache(false);
            ImageIO.write(screenshot, quality, bos);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RemoteException("Error taking screenshot", e);
        }
    }

    @Override
    public void mouseMove(int x, int y) throws RemoteException {
        try {
            Robot robot = new Robot();
            robot.mouseMove(x, y);
        } catch (AWTException e) {
            throw new RemoteException("Error moving mouse", e);
        }
    }

    @Override
    public void mouseClick(int button) throws RemoteException {
        try {
            Robot robot = new Robot();
            robot.mousePress(button);
            robot.mouseRelease(button);
        } catch (AWTException e) {
            throw new RemoteException("Error clicking mouse", e);
        }
    }

    @Override
    public void mousePress(int button) throws RemoteException {
        try {
            Robot robot = new Robot();
            robot.mousePress(button);
        } catch (AWTException e) {
            throw new RemoteException("Error pressing mouse", e);
        }
    }

    @Override
    public void mouseRelease(int button) throws RemoteException {
        try {
            Robot robot = new Robot();
            robot.mouseRelease(button);
        } catch (AWTException e) {
            throw new RemoteException("Error releasing mouse", e);
        }
    }

    @Override
    public void mouseWheelServer(int wheel_amt) throws RemoteException {
        try {
            Robot robot = new Robot();
            robot.mouseWheel(wheel_amt);
        } catch (Exception e) {
            throw new RemoteException("Error moving mouse wheel", e);
        }
    }

    @Override
    public void keyPress(int keyCode) throws RemoteException {
        try {
            Robot robot = new Robot();
            robot.keyPress(keyCode);
        } catch (AWTException e) {
            throw new RemoteException("Error pressing key", e);
        }
    }

    @Override
    public void keyRelease(int keyCode) throws RemoteException {
        try {
            Robot robot = new Robot();
            robot.keyRelease(keyCode);
        } catch (AWTException e) {
            throw new RemoteException("Error releasing key", e);
        }
    }

    @Override
    public void keyType(char character) throws RemoteException {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(character));
            robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(character));
        } catch (AWTException e) {
            throw new RemoteException("Error typing key", e);
        }
    }

    @Override
    public double getCpuUsage() throws RemoteException {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return osBean.getSystemLoadAverage() * 100; /* Posible fallito */
    }

    @Override
    public double getMemoryUsage() throws RemoteException {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        return (1 - (double) freeMemory / totalMemory) * 100;
    }

    @Override
    public ComputerInfo getHardwareInfo(String osName) throws RemoteException {
        ComputerInfo computerInfo = new ComputerInfo(osName);
        return computerInfo;
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        System.out.println("Received message: " + message);
    }

    @Override
    public void sendFile(String fileName, byte[] fileData) throws RemoteException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(fileData);
            fileOutputStream.close();
            System.out.println("File received and saved: " + fileName);
        } catch (IOException e) {
            throw new RemoteException("Error sending file", e);
        }
    }

    @Override
    public String getMessage() throws RemoteException {
        return "Hola soy un mensaje";
    }
}
