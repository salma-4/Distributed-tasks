import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloader implements Runnable {
       private String fileURL;
        private String fileDestination;

        public FileDownloader(String fileURL, String fileDestination) {
            this.fileURL = fileURL;
            this.fileDestination = fileDestination;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(fileURL);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(fileDestination);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                fileOutputStream.close();
                inputStream.close();

                System.out.println("File downloaded successfully: " + fileDestination);
            } catch (IOException e) {
                System.err.println("Error downloading file: " + e.getMessage());
            }
        }
    }