package stud;

public class DataProcessorFactory {
    static public DataProcessor getDataProcessor(String fileType, DataManager dataManager) {
        if (fileType.equalsIgnoreCase("CSV")) {
            return new CSVFileHandler(dataManager.getExistingGroups(), dataManager.getStudents(), dataManager.getAttendanceRecords(), dataManager.getFilteredAttendanceRecords());
        } else if (fileType.equalsIgnoreCase("Excel")) {
            return new ExcelFileHandler(dataManager.getExistingGroups(), dataManager.getStudents(), dataManager.getAttendanceRecords(), dataManager.getFilteredAttendanceRecords());
        } else if (fileType.equalsIgnoreCase("PDF")){
            return new PDFFileHandler(dataManager.getExistingGroups(), dataManager.getStudents(), dataManager.getAttendanceRecords(), dataManager.getFilteredAttendanceRecords());
        }
        return null;
    }
}
