public class Main {
    public static void main(String[] args) {
        String file1URL = "https://sample-videos.com/text/Sample-text-file-10kb.txt";
        String file2URL = "https://filesampleshub.com/download/document/txt/sample2.txt";
        String file1Destination = "C:\\Users\\salma\\Desktop\\distributed\\FileDownloader\\downloads\\file1.txt";
        String file2Destination = "C:\\Users\\salma\\Desktop\\distributed\\FileDownloader\\downloads\\file2.txt";


        Thread file1Thread = new Thread(new FileDownloader(file1URL, file1Destination));
        Thread file2Thread = new Thread(new FileDownloader(file2URL, file2Destination));

        file1Thread.start();
        file2Thread.start();
    }
}
