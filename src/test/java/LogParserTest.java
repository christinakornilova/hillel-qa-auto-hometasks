import hometask02.LogData;
import hometask02.LogParser;
import org.junit.*;


import java.io.*;
import java.util.List;

public class LogParserTest extends TestBase {

    private static final String fileName = "src/test/resources/simplefiles/test.txt";
    private static final String path = "src/test/resources/simplefiles";
    private static final String logFilePath = "src/test/resources/testlog";
    File destination = new File("target/out.log");

    @Test
    public void testGetFileContentToArrayLinesCount() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        String[] arr = LogParser.getFileContentToArray(fileInputStream);
        Assert.assertEquals("Lines count in array and file are different.", 3, arr.length);
    }

    @Test
    public void testGetFileContentToArrayArrayContent() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        String[] arr = LogParser.getFileContentToArray(fileInputStream);
        for (int i = 0; i < arr.length; i++) {
            Assert.assertEquals("Lines content in array and file are different", (i+1) + " line", arr[i]);
        }
    }

    @Test
    public void testwriteAllFilesContentToArrayList() {
        List<String> result = LogParser.writeAllFilesContentToArrayList(path);
        Assert.assertEquals("Lines count in both files and list size should be equal", 8, result.size());
    }

    @Test
    public void testRemoveUnnecessaryInfoSortAndConcatEntriesWriteToFile() {
        List<String> result = LogParser.writeAllFilesContentToArrayList(logFilePath);
        List<LogData> list = LogParser.removeUnnecessaryInfo(result);
        Assert.assertEquals("List size should be equal to file " + logFilePath + " lines count", 6, list.size());

        list = LogParser.sortAndFilterListData(list);
        Assert.assertEquals("Filtered lines count from test_log file should be 2", 2, list.size());

        list = LogParser.concatSameTimeTransactions(list);

        Assert.assertEquals("Concat lines count from test_log file should be 1", 1, list.size());
        Assert.assertEquals("", "Metro-1,Metro-11,Metro-12,Metro-13,Metro-15,Metro-2,Metro-28,Metro-29,Metro-3,Metro-30,Metro-33,Metro-4,Metro-5,Metro-8,Metro-9,Metro-10,Metro-11,Metro-12,Metro-14,Metro-15", list.get(0).getInfo().trim());

        LogParser.writeToFile(list, destination);
        File tmp = new File("target/out.log");
        Assert.assertTrue("No file created", tmp.exists());
    }


}
