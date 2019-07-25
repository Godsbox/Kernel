package com.lzj.arch;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {
        String [] oldArray;

        oldArray = new String []{"2","4","9","8"};
        sortString(oldArray);
        oldArray = new String []{"21","1","98","89"};
        sortString(oldArray);
        oldArray = new String []{"98","18","6","59"};
        sortString(oldArray);
        oldArray = new String []{"a","b","c","d"};
        sortString(oldArray);
        oldArray = new String []{"ab","cb","ba","dd"};
        sortString(oldArray);
        oldArray = new String []{"dc","db","cb","ca"};
        sortString(oldArray);
        oldArray = new String []{"a5","a6","a3","a1"};
        sortString(oldArray);
        oldArray = new String []{"asdfa453534","asgvasfg45345","asdfgas243536","2asvga435"};
        sortString(oldArray);
        /*newArray = new String []{"1","10","4","6","8"};
        diff(oldArray, newArray);

        oldArray = new String []{"11","12","2","4","6","9",};
        newArray = new String []{"1","10","4","6","8"};
        diff(oldArray, newArray);

        oldArray = new String []{"10","2","4","6","7","9"};
        newArray = new String []{"1","10","4","6","8","9"};
        diff(oldArray, newArray);*/
    }

    private void sortString(String [] md5Str){

        Arrays.sort(md5Str,String.CASE_INSENSITIVE_ORDER);
        System.out.println("sort md5 == "+Arrays.toString(md5Str));

    }

    private void diff(String[] old, String[] news) {

        int i = 0;
        int j = 0;
        int flag;
        List<String> delete = new ArrayList<>();
        List<String> adds = new ArrayList<>();
        while (true){
            if(i==old.length){
                for (int k = j; k < news.length; k++) {
                    adds.add(news[k]);
                }
                break;
            }
            if(j==news.length){
                for (int f = i; f < old.length; f++) {
                    delete.add(old[f]);
                }
                break;
            }

            String olds = old[i];
            String newss = news[j];
            flag = olds.compareTo(newss);
            if(flag==0){
                i++;
                j++;
                continue;
            }
            if(flag<0){
                delete.add(olds);
                i++;
                continue;
            }
            if(flag>0){
                adds.add(newss);
                j++;
                continue;
            }
        }

        System.out.println(Arrays.toString(old) + "\n" + Arrays.toString(news) + "\ndeleted: " + delete + "\nadded: " + adds + "\n");

    }
}