package isihop.fr.computerspy;

import java.util.Arrays;
import java.util.List;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.hardware.PowerSource;
import oshi.hardware.Sensors;
import oshi.hardware.SoundCard;
import oshi.hardware.UsbDevice;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

public class ComputerSpy {

    public static void main(String[] args) 
    {
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem os = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        CentralProcessor processor = hardware.getProcessor();
        Sensors sensors = hardware.getSensors();
        ComputerSystem computer = hardware.getComputerSystem();
        List<GraphicsCard> gcards = hardware.getGraphicsCards();
        List<Display> gdisps = hardware.getDisplays();
        List<HWDiskStore> hwds = hardware.getDiskStores();
        List<SoundCard> scards = hardware.getSoundCards();
        List<UsbDevice> usbs = hardware.getUsbDevices(false);
        List<PowerSource> psources = hardware.getPowerSources();
        List<NetworkIF> cnets = hardware.getNetworkIFs(true);

        header();
        infosystem(os);
        infoProcessor(processor);
        infoSensors(sensors);
        infoMemory(hardware);
        infoDisk(hwds);
        computer(computer);
        graphicsCards(gcards);
        monitors(gdisps);
        soundCards(scards);
        usbDevices(usbs); 
        powers(psources);
        networks(cnets);
    }

    /**
     * ***************************
     * Entete
     ****************************
     */
    private static void header() {
        System.out.println("\n================================");
        System.out.println("     Computer spy v1.0");
        System.out.println("isihop.fr : Tondeur Hervé (2025)");
        System.out.println("================================");
    }
    
    /******************************
     * 
     * @param os 
     ******************************/
    private static void infosystem(OperatingSystem os)
    {
        System.out.println("\n=== Informations sur le système ===");
        System.out.println();
        System.out.println("Système d'exploitation : " + os);
        System.out.println("Architecture : " + System.getProperty("os.arch"));
        System.out.println("Uptime :"+FormatUtil.formatElapsedSecs(os.getSystemUptime()));
        System.out.println("Nombre de Threads :"+os.getThreadCount());

    }
    
    /******************************
     * 
     * @param processor 
     ******************************/
    private static void infoProcessor(CentralProcessor processor)
    {
        try {
            System.out.println("\n=== Informations sur le processeur ===");
            System.out.println("Nom : " + processor.getProcessorIdentifier().getName());
            System.out.println("Fabricant : " + processor.getProcessorIdentifier().getVendor());
            System.out.println("Architecture : " + processor.getProcessorIdentifier().getMicroarchitecture());
            System.out.println("Fréquence maximale : " + FormatUtil.formatHertz(processor.getMaxFreq()));
            System.out.println("Nombre de cœurs physiques : " + processor.getPhysicalProcessorCount());
            System.out.println("Nombre de cœurs logiques : " + processor.getLogicalProcessorCount());
            System.out.println("Identifiant : " + processor.getProcessorIdentifier().getIdentifier());
            
            System.out.println("\n=== Charge CPU ===");
            for (int i = 0; i < processor.getCurrentFreq().length; i++) {
                System.out.println("Fréquence actuelle CPU [" + i + "] : " + FormatUtil.formatHertz(processor.getCurrentFreq()[i]));
            }
            // Capture des ticks initiaux
            long[][] oldTicks = processor.getProcessorCpuLoadTicks();
            
            // Attente d'une seconde
            Thread.sleep(1000);
            double[] load = processor.getProcessorCpuLoadBetweenTicks(oldTicks);
            for (int i = 0; i < load.length; i++) {
                System.out.printf("Cœur %d : %.1f%%%n", i, load[i] * 100);
            }
        } catch (InterruptedException ex) {
            System.getLogger(ComputerSpy.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    /**************************
     * 
     * @param sensors 
     **************************/
    private static void infoSensors(Sensors sensors)
    {
        System.out.println("\n=== Température et capteurs ===");
        System.out.println("Température CPU : " + sensors.getCpuTemperature() + " °C");
        System.out.println("Tension CPU : " + sensors.getCpuVoltage() + " V");
        System.out.println("Ventilateurs : " + sensors.getFanSpeeds().length + " détectés");

    }
    
    /***************************
     * 
     * @param hardware 
     ***************************/
    private static void infoMemory(HardwareAbstractionLayer hardware)
    {
        System.out.println("\n=== Mémoire ===");
        System.out.println("Mémoire totale : " + FormatUtil.formatBytes(hardware.getMemory().getTotal()));
        System.out.println("Mémoire disponible : " + FormatUtil.formatBytes(hardware.getMemory().getAvailable()));
    }
    
    /****************************
     * 
     * @param hwds 
     ****************************/
    private static void infoDisk(List<HWDiskStore> hwds)
    {
            System.out.println("\n=== Disques ===");
        for (HWDiskStore hwd : hwds) 
        {
            System.out.println("Disque [" + hwd.getName() + "] : " + hwd.getModel());
            System.out.println("\tNuméro de série :" + " " + hwd.getSerial());
            System.out.println("\tTaille : " + FormatUtil.formatValue(hwd.getSize(), "o"));
            int numpart = 0;
            for (HWPartition hpart : hwd.getPartitions()) {
                System.out.println("\t\t --Partition [" + numpart + "]--");
                System.out.println("\t\tNom :" + hpart.getName());
                System.out.println("\t\tIdentification :" + hpart.getIdentification());
                System.out.println("\t\tTaille :" + FormatUtil.formatValue(hpart.getSize(), "o"));
                System.out.println("\t\tType :" + hpart.getType());
                System.out.println("\t\tUUID :" + hpart.getUuid());
                System.out.println("\t\tMajor.minor version :" + hpart.getMajor() + "." + hpart.getMinor());
                numpart++;
            }
            System.out.println("");
        }
    }
    
    
    /***************************
     * 
     * @param computer 
     ***************************/
    private static void computer(ComputerSystem computer)
    {
        System.out.println("\n=== Chassis ===");
        System.out.println("Chassis : " + computer.getBaseboard());
        System.out.println("Fabricant : " + computer.getManufacturer());
        System.out.println("Modèle : " + computer.getModel());
        System.out.println("Numéro de série : " + computer.getSerialNumber());
        System.out.println("Firmware : " + computer.getFirmware());
        System.out.println("HardWare UUID : " + computer.getHardwareUUID());

    }
    
    /**************************
     * 
     * @param gcards 
     **************************/
    private static void graphicsCards(List<GraphicsCard> gcards)
    {
                System.out.println("\n=== Cartes graphiques ===");
        int numcard = 0;
        if (!gcards.isEmpty())
        {
        for (GraphicsCard gcard : gcards) {
            System.out.println("--Carte [" + numcard + "]--");
            System.out.println("\tNom :" + gcard.getName());
            System.out.println("\tFabricant :" + gcard.getVendor());
            System.out.println("\tVersion :" + gcard.getVersionInfo());
            System.out.println("\tIdentifiant :" + gcard.getDeviceId());
            System.out.println("\tVRAM :" + FormatUtil.formatValue(gcard.getVRam(), "o"));
            numcard++;
        }
        }
        else
        {
            System.out.println("Non reconnu !");
        }
        System.out.println("");
    }
    
    
    /*************************
     * 
     * @param gdisps 
     *************************/
    private static void monitors(List<Display> gdisps)
    {
        System.out.println("\n=== Moniteurs ===");
        int numdisp = 0;        
        if (!gdisps.isEmpty()) {
            for (Display gdisp : gdisps) {
                System.out.println("--Carte [" + numdisp + "]--");
                System.out.println("\tNom :" + Arrays.toString(gdisp.getEdid()));
                numdisp++;
            }
        } else {
            System.out.println("Non reconnu !");
        }
        System.out.println("");

    }

    
    /**************************
     * 
     * @param scards 
     **************************/
    private static void soundCards(List<SoundCard> scards)
    {
        System.out.println("\n=== Cartes son ===");
        int numsound = 0;
        if (!scards.isEmpty()) {
            for (SoundCard scard : scards) {
                System.out.println("--Carte [" + numsound + "]--");
                System.out.println("\tNom :" + scard.getName());
                System.out.println("\tCodec :" + scard.getCodec());
                System.out.println("\tVersion :" + scard.getDriverVersion());
                numsound++;
            }
        } else {
            System.out.println("Non reconnu !");
        }
        System.out.println("");

    }

    /***************************
     * 
     * @param usbs 
     **************************/
    private static void usbDevices(List<UsbDevice> usbs) 
    {
        System.out.println("\n=== USB devices ===");
        int numusb = 0;
        for (UsbDevice usb : usbs) {
            System.out.println("--Device USB [" + numusb + "]--");
            System.out.println("\tNom :" + usb.getName());
            System.out.println("\tFabricant :" + usb.getVendor());
            System.out.println("\tIdentifiant Fabricant :" + usb.getVendorId());
            System.out.println("\tNuméro de série :" + usb.getSerialNumber());
            System.out.println("\tIdentifiant produit :" + usb.getProductId());
            System.out.println("\tIdentifiant unique :" + usb.getUniqueDeviceId());
            System.out.println("\tMatériel connecté :" + usb.getConnectedDevices().toString());
            numusb++;
        }
        System.out.println("");
    }
    
    /****************************
     * 
     * @param psources 
     ****************************/
    private static void powers(List<PowerSource> psources) 
    {
        System.out.println("\n=== Alimentations ===");
        int numalim = 0;
        if (!psources.isEmpty()) {
            for (PowerSource psource : psources) {
                System.out.println("--Batterie [" + numalim + "]--");
                System.out.println("\tNom :" + psource.getName());
                System.out.println("\tNom device :" + psource.getDeviceName());
                System.out.println("\tFabricant :" + psource.getManufacturer());
                System.out.println("\tNuméro de série :" + psource.getSerialNumber());
                System.out.println("\tChimie :" + psource.getChemistry());
                System.out.println("\tNombre de cycles :" + psource.getCycleCount());
                numalim++;
            }
        } else {
            System.out.println("Non reconnu !");
        }
        System.out.println("");
    }

    /*************************
     * 
     * @param cnets 
     *************************/
    private static void networks(List<NetworkIF> cnets) 
    {
        System.out.println("\n=== Cartes Réseaux ===");
        int numnet = 0;
        for (NetworkIF cnet : cnets) {
            System.out.println("--Carte [" + numnet + "]--");
            System.out.println("\tNom :" + cnet.getName());
            System.out.println("\tNom logique :" + cnet.getDisplayName());
            System.out.println("\tIndex :" + cnet.getIndex());
            System.out.println("\tAdresse MAC :" + cnet.getMacaddr());
            System.out.println("\tVitesse réseau :" + FormatUtil.formatValue(cnet.getSpeed(), "o"));
            System.out.println("\tIP V4 :" + Arrays.toString(cnet.getIPv4addr()));
            System.out.println("\tIP V6 :" + Arrays.toString(cnet.getIPv6addr()));
            System.out.println("\tMTU :" + cnet.getMTU() + " octets.");
            System.out.println("\tMasque sous réseau :" + Arrays.toString(cnet.getSubnetMasks()));

            numnet++;
        }
        System.out.println("");
    }
    
}
