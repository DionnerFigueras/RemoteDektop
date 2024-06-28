package bus.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import gui.remote.ComputerInfo;

public interface RemoteDesktopInterface extends Remote{
// Captura de pantalla remota
    byte[] takeScreenshotServer(String quality) throws RemoteException;

    // Métodos para control de ratón
    void mouseMove(int x, int y) throws RemoteException;

    void mouseClick(int button) throws RemoteException;

    void mousePress(int button) throws RemoteException;

    void mouseRelease(int button) throws RemoteException;

    void mouseWheelServer(int wheel_amt) throws RemoteException;
    
    // Métodos para control de teclado
    void keyPress(int keyCode) throws RemoteException;

    void keyRelease(int keyCode) throws RemoteException;

    void keyType(char character) throws RemoteException;

    // Métodos para lectura de hardware
    double getCpuUsage() throws RemoteException;

    double getMemoryUsage() throws RemoteException;

    ComputerInfo getHardwareInfo(String osName) throws RemoteException;

    // Método para chat
    void sendMessage(String message) throws RemoteException;

    // Método para enviar archivos
    void sendFile(String fileName, byte[] fileData) throws RemoteException;

    //Devolver un mensaje
    String getMessage() throws RemoteException;
}
