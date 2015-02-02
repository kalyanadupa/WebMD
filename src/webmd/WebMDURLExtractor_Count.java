/**
 *
 */
package webmd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author cnx471
 *
 */
public class WebMDURLExtractor_Count {

    /**
     * @param args
     * @throws IOException
     *
     */
    public static void main(String[] args) throws IOException {

        int count = 0;
        HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
        HashMap<String, Integer> hashmap1 = new HashMap<String, Integer>();
        HashMap<String, Integer> userMap = new HashMap<String, Integer>();
        HashMap<String, Integer> helProMap = new HashMap<String, Integer>();
        HashMap<String, Integer> staffMap = new HashMap<String, Integer>();
        List<String> uniqueSet = new ArrayList<String>();
        Pattern p8 = Pattern.compile(".{1,50}www.{1,500}|.{1,50}http.{1,500}");
        String[] fileNames = {"webmd_addiction", "webmd_adhd", "webmd_breast_cancer", "webmd_diabetes", "webmd_diet", "webmd_fkids", "webmd_heart", "webmd_ms", "webmd_pain", "webmd_sexualhealth"};
        //String[] fileNames = {"webmd_addiction"};
        //*******************************For the matchings of staff and qid**************************/////
        
        for (String fileLog : fileNames) {
            int HelPro = 0;
            int staffCount = 0;
            int q1Post = 0;
            int q2Post = 0;
            int q3Post = 0;
            int staffPost = 0;
            int helProPost = 0;
            hashmap = new HashMap<String, Integer>();
            hashmap1 = new HashMap<String, Integer>();
            userMap = new HashMap<String, Integer>();
            helProMap = new HashMap<String, Integer>();
            staffMap = new HashMap<String, Integer>();
            uniqueSet = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new FileReader("data/" + fileLog + ".csv"));
            List<String> qidArray = new ArrayList<String>();
            BufferedReader in = new BufferedReader(new FileReader("data/" + fileLog + "_staff_qid.csv")); // qid file 

            BufferedWriter writer = new BufferedWriter(new FileWriter("test/"+fileLog + "_PU.txt"));

            String s = in.readLine();
            while ((s = in.readLine()) != null) {
                qidArray.add(s);
            }
            
            while (br.ready()) {
                
                String check = br.readLine();
                String[] tokenizedTerms1 = check.split("\"");
                //String nameToken1 = tokenizedTerms1[3];
                String[] tokenizedTerms = check.split(",");
                String token = tokenizedTerms[1];
                String nameToken = tokenizedTerms1[3];
                              
//                if(nameToken.length() < 3)
//                    continue;
                

                if (nameToken.length() > 3) {
                    if (nameToken.charAt(nameToken.length() - 1) == ' ') {
                        nameToken = nameToken.replace(nameToken.substring(nameToken.length() - 1), "");
                    }
                }
                
                if(check.contains("http") || check.contains("www")){
                    if (nameToken.toUpperCase().contains(", MSN") || nameToken.toUpperCase().contains(", RNP") || nameToken.toUpperCase().contains(", CDOE") || nameToken.toUpperCase().contains(", MD") || nameToken.toUpperCase().contains(", MPH") || nameToken.toUpperCase().contains(", PHD") || nameToken.toUpperCase().contains(", PT") || nameToken.toUpperCase().contains(", DSc") || nameToken.toUpperCase().contains(", NCS") || nameToken.toUpperCase().contains(", MSCS") || nameToken.toUpperCase().contains(", RN")) {
                        helProPost++;
                        if (helProMap.get(nameToken) == null) {
                            helProMap.put(nameToken, 1);
                        } else {
                            helProMap.put(nameToken, helProMap.get(nameToken) + 1);
                        }
                        continue;
                    }
                    if (nameToken.contains("WebMD_Staff")) {
                        staffPost++;
                        
                        if (staffMap.get(nameToken) == null) {
                            staffMap.put(nameToken, 1);
                        } else {
                            staffMap.put(nameToken, staffMap.get(nameToken) + 1);
                        }
                        continue;
                    }

                    if (userMap.get(nameToken) == null) {
                        userMap.put(nameToken, 1);
                    } else {
                        userMap.put(nameToken, userMap.get(nameToken) + 1);
                    }
                }
            }
            br.close();
            //System.out.println("Total Size = "+ userMap.size() +"\n staff Size"+ staffMap.size() +"\n helPro"+ helProMap.size());
            
//            ValueComparator<String, Integer> comparator = new ValueComparator<String, Integer>(userMap);
//            Map<String, Integer> sortedUserMap = new TreeMap<String, Integer>(comparator);
//            sortedUserMap.putAll(userMap);
//
//            List<String> sortedList = new ArrayList<String>(sortedUserMap.keySet());

            //System.out.println(sortedUserMap);
            
//            System.out.println(sortedList);
            SortedSet<Map.Entry<String, Integer>> sortedUserMap = entriesSortedByValues(userMap);
            //System.out.println(sortedUserMap);
            br = new BufferedReader(new FileReader("data/" + fileLog + ".csv"));
            int userSize = sortedUserMap.size();
            //System.out.println("sorted size: " + sortedUserMap.size() +" original size"+userMap.size() );
            
            int top25 = userSize / 4;
            int mid50 = userSize - top25;
            HelPro = helProMap.size();
            staffCount = staffMap.size();
            
            List<String> sortedUserList = new ArrayList<String>();            
            for (Entry<String, Integer> ent : sortedUserMap) {
                sortedUserList.add(ent.getKey());
            }
            
//            for (Map.Entry<String, Integer> entry : sortedUserMap.entrySet()) {
//                int selectedUserIndex = sortedList.indexOf(entry.getKey());
//                if(selectedUserIndex <= top25)
//                    System.out.print("First Class \t");
//                else if((selectedUserIndex <= mid50)&&(selectedUserIndex > top25))
//                    System.out.print("Second Class \t");
//                else
//                    System.out.print("Third Class \t");
//                System.out.println(entry.getKey() +"\t"+ entry.getValue());
//            }
            q1Post = 0; 
            q2Post = 0;
            q3Post = 0;
            while (br.ready()) {
                String check = br.readLine();
                String[] tokenizedTerms = check.split("\"");
                String token = tokenizedTerms[1];
                String nameToken = tokenizedTerms[3];
                
                
                if (nameToken.length() > 3) {
                    if (nameToken.charAt(nameToken.length() - 1) == ' ') {
                        nameToken = nameToken.replace(nameToken.substring(nameToken.length() - 1), "");
                    }
                }

                int selectedUserIndex = sortedUserList.indexOf(nameToken);
                if((selectedUserIndex < mid50)&&(selectedUserIndex <= top25)&&(selectedUserIndex != -1)){
                    if ((check.contains("www")) || (check.contains("http"))) {
                        q1Post ++;
                        String check2 = " ";
                        String check3 = " ";
                        Matcher m8 = p8.matcher(check);
                        List<String> matchstring8 = new ArrayList<String>();

                        while (m8.find()) {
                            check2 = check2 + "" + m8.group();
                            matchstring8.add(m8.group());
//                    System.out.println(m8.group());
                        }

                        Pattern p9 = Pattern.compile("https?\\:\\/\\/[\\-w\\.]*(\\:\\d+)?([\\w\\/\\_\\-\\.\\=\\?\\&\\%\\+\\@\\^\\~\\!\\#\\$]*)?[^www]|www\\.(\\:\\d+)?([\\w\\/\\_\\-\\.\\=\\?\\&\\%\\+\\@\\^\\~\\!\\#\\$]*)?[^www]");
                        Matcher m9 = p9.matcher(check2);
                        List<String> matchstring9 = new ArrayList<String>();
                        while (m9.find()) {
                            check3 = check3 + "" + m9.group();
                            matchstring9.add(m9.group());
//                      System.out.println(m9.group());
                        }

                        Pattern p10 = Pattern.compile("www.*?(\\.com(?=\\W)|\\.com\\,?\\)?|\\.org(?=\\W)|\\.ORG(?=\\W)|\\.net(?=\\W)|\\.gov(?=\\W)|\\.co.uk|\\.html|\\.htm|\\.asp|\\.aspx|\\.edu|\\.us|treatment|\\-men(?=\\W)|\\.pdf|\\.ca\\/servlet|\\.ch(?=\\W)|\\.coream(?=\\W)|\\.ee(?=\\W))|www.*?(?=\\/)|(?<=http\\:\\/\\/).*?(?=\\/)|(?<=https\\:\\/\\/).*?(?=\\/)");
                        Matcher m10 = p10.matcher(check3);
                        List<String> matchstring10 = new ArrayList<String>();

                        while (m10.find()) {
                            matchstring10.add(m10.group());
//				  System.out.println(m10.group());

                            //*******************************Unique set created and items put in HashMap (hashMap)**************************/////
                            for (int i = 0; i < matchstring10.size(); i++) {
                                if (uniqueSet.contains(matchstring10.get(i))) {
                                    int elementCount = Integer.parseInt(hashmap.get(matchstring10.get(i)).toString());
                                    elementCount++;
                                    hashmap.put(matchstring10.get(i), elementCount);
                                    hashmap1.put(matchstring10.get(i), elementCount);
                                } else {
                                    uniqueSet.add(matchstring10.get(i));
                                    hashmap.put(matchstring10.get(i), 1);
                                    hashmap1.put(matchstring10.get(i), 1);
                                }
                            }
                        }
                    }
                }
                else if ((selectedUserIndex <= mid50) && (selectedUserIndex > top25) && (selectedUserIndex != -1)) {
                    if ((check.contains("www")) | (check.contains("http"))) {
                        q2Post++;
                        String check2 = " ";
                        String check3 = " ";
                        Matcher m8 = p8.matcher(check);
                        List<String> matchstring8 = new ArrayList<String>();

                        while (m8.find()) {
                            check2 = check2 + "" + m8.group();
                            matchstring8.add(m8.group());
//                    System.out.println(m8.group());
                        }

                        Pattern p9 = Pattern.compile("https?\\:\\/\\/[\\-w\\.]*(\\:\\d+)?([\\w\\/\\_\\-\\.\\=\\?\\&\\%\\+\\@\\^\\~\\!\\#\\$]*)?[^www]|www\\.(\\:\\d+)?([\\w\\/\\_\\-\\.\\=\\?\\&\\%\\+\\@\\^\\~\\!\\#\\$]*)?[^www]");
                        Matcher m9 = p9.matcher(check2);
                        List<String> matchstring9 = new ArrayList<String>();
                        while (m9.find()) {
                            check3 = check3 + "" + m9.group();
                            matchstring9.add(m9.group());
//                      System.out.println(m9.group());
                        }

                        Pattern p10 = Pattern.compile("www.*?(\\.com(?=\\W)|\\.com\\,?\\)?|\\.org(?=\\W)|\\.ORG(?=\\W)|\\.net(?=\\W)|\\.gov(?=\\W)|\\.co.uk|\\.html|\\.htm|\\.asp|\\.aspx|\\.edu|\\.us|treatment|\\-men(?=\\W)|\\.pdf|\\.ca\\/servlet|\\.ch(?=\\W)|\\.coream(?=\\W)|\\.ee(?=\\W))|www.*?(?=\\/)|(?<=http\\:\\/\\/).*?(?=\\/)|(?<=https\\:\\/\\/).*?(?=\\/)");
                        Matcher m10 = p10.matcher(check3);
                        List<String> matchstring10 = new ArrayList<String>();

                        while (m10.find()) {
                            matchstring10.add(m10.group());
//				  System.out.println(m10.group());

                            //*******************************Unique set created and items put in HashMap (hashMap)**************************/////
                            for (int i = 0; i < matchstring10.size(); i++) {
                                if (uniqueSet.contains(matchstring10.get(i))) {
                                    int elementCount = Integer.parseInt(hashmap.get(matchstring10.get(i)).toString());
                                    elementCount++;
                                    hashmap.put(matchstring10.get(i), elementCount);
                                    hashmap1.put(matchstring10.get(i), elementCount);
                                } else {
                                    uniqueSet.add(matchstring10.get(i));
                                    hashmap.put(matchstring10.get(i), 1);
                                    hashmap1.put(matchstring10.get(i), 1);
                                }
                            }
                        }

                    }
                }
                else if ((selectedUserIndex > mid50) && (selectedUserIndex > top25) && (selectedUserIndex != -1)) {
                    if ((check.contains("www")) | (check.contains("http"))) {
                        q3Post++;
                        String check2 = " ";
                        String check3 = " ";
                        Matcher m8 = p8.matcher(check);
                        List<String> matchstring8 = new ArrayList<String>();

                        while (m8.find()) {
                            check2 = check2 + "" + m8.group();
                            matchstring8.add(m8.group());
//                    System.out.println(m8.group());
                        }

                        Pattern p9 = Pattern.compile("https?\\:\\/\\/[\\-w\\.]*(\\:\\d+)?([\\w\\/\\_\\-\\.\\=\\?\\&\\%\\+\\@\\^\\~\\!\\#\\$]*)?[^www]|www\\.(\\:\\d+)?([\\w\\/\\_\\-\\.\\=\\?\\&\\%\\+\\@\\^\\~\\!\\#\\$]*)?[^www]");
                        Matcher m9 = p9.matcher(check2);
                        List<String> matchstring9 = new ArrayList<String>();
                        while (m9.find()) {
                            check3 = check3 + "" + m9.group();
                            matchstring9.add(m9.group());
//                      System.out.println(m9.group());
                        }

                        Pattern p10 = Pattern.compile("www.*?(\\.com(?=\\W)|\\.com\\,?\\)?|\\.org(?=\\W)|\\.ORG(?=\\W)|\\.net(?=\\W)|\\.gov(?=\\W)|\\.co.uk|\\.html|\\.htm|\\.asp|\\.aspx|\\.edu|\\.us|treatment|\\-men(?=\\W)|\\.pdf|\\.ca\\/servlet|\\.ch(?=\\W)|\\.coream(?=\\W)|\\.ee(?=\\W))|www.*?(?=\\/)|(?<=http\\:\\/\\/).*?(?=\\/)|(?<=https\\:\\/\\/).*?(?=\\/)");
                        Matcher m10 = p10.matcher(check3);
                        List<String> matchstring10 = new ArrayList<String>();

                        while (m10.find()) {
                            matchstring10.add(m10.group());
//				  System.out.println(m10.group());

                            //*******************************Unique set created and items put in HashMap (hashMap)**************************/////
                            for (int i = 0; i < matchstring10.size(); i++) {
                                if (uniqueSet.contains(matchstring10.get(i))) {
                                    int elementCount = Integer.parseInt(hashmap.get(matchstring10.get(i)).toString());
                                    elementCount++;
                                    hashmap.put(matchstring10.get(i), elementCount);
                                    hashmap1.put(matchstring10.get(i), elementCount);
                                } else {
                                    uniqueSet.add(matchstring10.get(i));
                                    hashmap.put(matchstring10.get(i), 1);
                                    hashmap1.put(matchstring10.get(i), 1);
                                }
                            }
                        }

                    }

                }                

                //*******************************Find Phenotype (Website Link)**************************/////
                
                    

            }
            br.close();
//            Q1 User Count: 
//Q2 User Count: 
//Q4 User Count: 
//Health Pro User Count: 
//Staff User Count: 
//
//Q1 user Post Count
//Q2 - Q3 user Post Count
//Q4 user Post Count
//Health Pro user Post Count
//WebMD Staff user Post Count
            int q4Post = 0;
            mid50 = userSize - top25 * 2;
            System.out.println(fileLog);
            System.out.println(top25);
            System.out.println(mid50);
            q4Post = userSize - top25 - mid50;
            System.out.println(q4Post);
            System.out.println(HelPro );
            System.out.println(staffCount);            
            System.out.println("");
            System.out.println(q1Post);
            System.out.println(q2Post);
            System.out.println(q3Post);
            System.out.println(helProPost);
            System.out.println(staffPost);
            System.out.println("\n\n");
            SortedSet<Map.Entry<String, Integer>> sortedJournals = entriesSortedByValues(hashmap);
            for (Entry<String, Integer> ent : sortedJournals) {
                writer.write(ent.getKey() + "\t" + ent.getValue() + '\n');
                //System.out.println(ent.getKey() + "\t" + ent.getValue());
            }
            
            writer.close();
            
        }

    }

    
    static class ValueComparator<K, V extends Comparable<V>> implements Comparator<K> {
        Map<K, V> map;
        public ValueComparator(Map<K, V> base) {
            this.map = base;
        }
        @Override
        public int compare(K o1, K o2) {
            return map.get(o2).compareTo(map.get(o1));
        }
    }
    
    static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        int res = e2.getValue().compareTo(e1.getValue());
                        return res != 0 ? res : 1; // Special fix to preserve items with equal values
                    }
                });
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
    
    public static LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)) {
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String) key, (Double) val);
                    break;
                }

            }

        }
        return sortedMap;
    }
}
