
import org.apache.zookeeper.KeeperException;
import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        if (args.length != 2) {
            System.out.println("Expecting parameters <number of workers> <path to worker jar file>");
            System.exit(1);
        }

        int numberOfWorkers = Integer.parseInt(args[0]);
        String pathToWorkerProgram = args[1];

        Autohealer autohealer = new Autohealer(numberOfWorkers, pathToWorkerProgram);
        autohealer.connectToZookeeper();
        autohealer.startWatchingWorkers();
        autohealer.run();
        autohealer.close();
    }
}
