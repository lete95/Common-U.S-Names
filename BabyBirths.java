import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
public class BabyBirths {
    public void printNames () {
    FileResource fr = new FileResource();
    for (CSVRecord rec : fr.getCSVParser(false)) {
        int numBorn = Integer.parseInt(rec.get(2));
        if (numBorn <= 100) {
            System.out.println("Name " + rec.get(0) +
                       " Gender " + rec.get(1) +
                       " Num Born " + rec.get(2));
        }
    }
    }
    public void readOneFile(int year){
       String fname="data/yob"+year+".txt";
       FileResource fr=new FileResource(fname);
       CSVParser parser =fr.getCSVParser(false);
       for(CSVRecord rec:parser){
           String name = rec.get(0);
           String gender=rec.get(1);
       }
       }
    public void totalBirths (FileResource fr){
        int totalBirths=0;int totalBoys=0;int totalGirls=0;
        int numnames=0;int boysnames=0;int girlsnames=0;
        for(CSVRecord rec:fr.getCSVParser(false)){
            int numBorn=Integer.parseInt(rec.get(2));
            totalBirths+=numBorn;
            if(rec.get(1).equals("M")){
                totalBoys+=numBorn;
                numnames++;
                boysnames++;
            }
            else{
                totalGirls+=numBorn;
                numnames++;
                girlsnames++;
            }
        }
        System.out.println("totalbirths = "+totalBirths);
        System.out.println("totalbirths girls = "+totalGirls);
        System.out.println("totalbirths boys = "+totalBoys);
        System.out.println("totalnames = "+numnames);
        System.out.println("totalgirls names = "+girlsnames);
        System.out.println("totalboys names = "+boysnames);
        }
    public void testTotalBirths(){
        FileResource fr=new FileResource();
        totalBirths(fr);
    }
        
        public int getRank(int year, String name, String gender) {
        int rank = 0;
        int tempRank = 0;
        int cgirls=0;
        FileResource fr = new FileResource("data/yob" + year + ".csv");
        CSVParser parser = fr.getCSVParser(false);
        for (CSVRecord record : parser) {
            tempRank++;
            String childName = record.get(0);
            String childGender = record.get(1);
            if(childGender.equals("F")){cgirls++;}
            if(childName.equals(name) && childGender.equals(gender)) {
                    rank = tempRank;
            }
        }
        if(gender.equals("M")){rank=rank-cgirls;}
        if(rank==0){
            rank=-1;
        }
        return rank;
    }
    public void testGetRank(){
    int ranking=getRank(1977,"Owen","M");
    System.out.println("El numero en el ranking es : "+ranking);
    }
    
    public String getName(int year, int rank, String gender) {
        int cgirls=0;
        String name=null;
        int crank=0;
        FileResource fr = new FileResource("data/yob" + year + ".csv");
        CSVParser parser = fr.getCSVParser(false);
        for (CSVRecord record : parser) {
            String childName = record.get(0);
            String childGender = record.get(1);
            if(childGender.equals("F")){cgirls++;}
            crank++;
            if(gender.equals("M")){
                if(crank==(rank-cgirls) && childGender.equals(gender)) {
                    name = record.get(0);
                }
            }
            if(gender.equals("F")){
                if(crank==rank && childGender.equals(gender)) {
                    name = record.get(0);
                }
            }
        }
        return name;
    }
    public void testGetName(){
    String name=getName(1982,450,"M");
    System.out.println("The name is: "+name);
        }
    
    public void whatIsNameInYear(String name, int year, int newYear,
    String gender) {
         // Defining variables
        String newname = ""; // inital string
        int rank = getRank(year, name, gender);
        if (rank != -1) { // rank of baby exists
            newname = getName(newYear, rank, gender);
            if (newname.equals("NO NAME")) {
                newname = "no one";
            }
        }
        else if (rank == -1) {
            newname = "no one";
        }
        if (gender.equals("M")) { // male baby
        System.out.println(name + " born in " + year + " would be " + newname
        + " if he was born in " + newYear);
        }
        else if (gender.equals("F")) { // female baby
        System.out.println(name + " born in " + year + " would be " + newname
        + " if she was born in " + newYear);
        }
    }
    
public int yearOfHighestRank (String name, String gender) {	
    int year=0;
    int startRank=0;
    DirectoryResource dr=new DirectoryResource();
    for(File f:dr.selectedFiles()){
        FileResource fr=new FileResource(f);
        String fName=f.getName();
        String find = "yob";
        int startPos=fName.indexOf(find);
        int currYear=Integer.parseInt(fName.substring(startPos+3,startPos+7));
        int currRank=getRank(currYear,name,gender);
        if(year==0){
            year=currYear;
            startRank=currRank;
        }
        if (currRank!=-1){
            if(currRank<startRank){
            year=currYear;
            startRank=currRank;
            }
        }
    }
    return year;
}
public void testyearOfHighestRank(){
    int year = yearOfHighestRank("Mich","M");
    System.out.println("The year of highest rank was: "+year);
}

public int getTotalBirthsRankedHigher (int year, String name, String gender){
    FileResource fr = new FileResource("data/yob"+year+".csv");
    CSVParser parser = fr.getCSVParser(false);
    int totalBirths = 0;
    int rank = getRank(year, name, gender);
    for (CSVRecord rec : fr.getCSVParser(false)) {
            if (rec.get(1).equals(gender)) {
                rank--;
		if (rank > 0) 
		  totalBirths += Integer.parseInt(rec.get(2));
	}
    }
    return totalBirths;
    }
public void testGetTotalBirthsRankedHigher(){
        int year = 1990;
        String name = "Drew";
        String gender = "M";
        int total = getTotalBirthsRankedHigher(year,name,gender);
        System.out.println("The total births ranked higher of the name "+name+" is "+total);
    }
public double getAverageRank(String name, String gender){
    int year=0;
    int startRank=0;
    DirectoryResource dr=new DirectoryResource();
    double count=0;
    double average=0;
    for(File f:dr.selectedFiles()){
        count++;
        FileResource fr=new FileResource(f);
        String fName=f.getName();
        String find = "yob";
        int startPos=fName.indexOf(find);
        int currYear=Integer.parseInt(fName.substring(startPos+3,startPos+7));
        int currRank=getRank(currYear,name,gender);
        if (currRank!=-1){
            average=average+currRank;
            }
        }
        double result= ((double)average)/count;
        return result;
    }    
public void testAverageRank(){
    double rank = getAverageRank("Robert","M");
    System.out.println("Average rank of name is " + rank);
}
}




















    


