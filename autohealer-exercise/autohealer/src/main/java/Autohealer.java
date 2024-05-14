import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Autohealer implements Watcher {

    private static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 3000;
    private static final String AUTOHEALER_ZNODES_PATH = "/workers";
    private final String pathToProgram;
    private final int numberOfWorkers;
    private ZooKeeper zooKeeper;

    public Autohealer(int numberOfWorkers, String pathToProgram) {
        this.numberOfWorkers = numberOfWorkers;
        this.pathToProgram = pathToProgram;
    }
    public void startWatchingWorkers() throws KeeperException, InterruptedException {
        // Check if the parent znode exists
        Stat stat = zooKeeper.exists(AUTOHEALER_ZNODES_PATH, false);
        if (stat == null) {
            // Create the parent znode if it doesn't exist
            zooKeeper.create(AUTOHEALER_ZNODES_PATH, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        launchWorkersIfNecessary();
    }

    public void connectToZookeeper() throws IOException {
        this.zooKeeper = new ZooKeeper(ZOOKEEPER_ADDRESS, SESSION_TIMEOUT, this);
    }

    public void run() throws InterruptedException {
        synchronized (zooKeeper) {
            zooKeeper.wait();
        }
    }

    public void close() throws InterruptedException {
        zooKeeper.close();
    }

    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("Successfully connected to Zookeeper");
                } else {
                    synchronized (zooKeeper) {
                        System.out.println("Disconnected from Zookeeper event");
                        zooKeeper.notifyAll();
                    }
                }
                break;
            case NodeDeleted:
                if (event.getPath().startsWith(AUTOHEALER_ZNODES_PATH + "/")) {
                    System.out.println("Worker deleted, launching a new one");
                    launchWorkersIfNecessary();
                }
                break;
            default:
                System.out.println("Unhandled ZooKeeper event: " + event.getType());
        }
    }

    private void launchWorkersIfNecessary() {
        try {
            Stat stat = new Stat();
            byte[] data = zooKeeper.getData(AUTOHEALER_ZNODES_PATH, false, stat);
            int numberOfActiveWorkers = stat.getNumChildren();

            for (int i = numberOfActiveWorkers; i < numberOfWorkers; i++) {
                startNewWorker();
            }
        } catch (KeeperException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void startNewWorker() throws IOException {
        Runtime.getRuntime().exec("java -jar " + pathToProgram);
    }
}
