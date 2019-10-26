import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Stream;

import frc.robot.Utils;

public class UtilsTest {

    static double testDouble = 69.0;
    static ArrayList<Double> testArray = new ArrayList<Double>();
    static ArrayList<Double> comparableArray = new ArrayList<Double>();
    static ArrayList<String> stringArray = new ArrayList<String>();
    static HashSet<String> testHashSet = new HashSet<String>();
    static ArrayList<Double> combinedArray = new ArrayList<Double>();
    
    static ArrayList<Double> newArray = new ArrayList<>();
    static ArrayList<Double> arrayToCopy = new ArrayList<>();

    static Pair<Double, Double> bounds = new Pair<>(2.0, 5.0);
    static Double arr[] = {2.0, 3.0, 5.0};
    static Stream<Integer> streamList = Stream.of(1,2,3);

    public static void testUtils() {
        testArray.add(1.0);
        testArray.add(2.0);
        testArray.add(11.45);

        comparableArray.add(1.0);
        comparableArray.add(2.0);
        comparableArray.add(11.45);

        for (int i = 0; i < 2; i++) {
            combinedArray.add(1.0);
            combinedArray.add(2.0);
            combinedArray.add(11.45);
        }

        testHashSet.add("test");
        testHashSet.add("test");
        testHashSet.add("test");

        stringArray.add("test");
        stringArray.add("test");
        stringArray.add("test");

        arrayToCopy.add(2.0);
        arrayToCopy.add(3.0);
        arrayToCopy.add(4.0);

        Tester.assertImpresiceEquals(Utils.metersToInches(testDouble), 2716.53, "Meters to inches test");
        Tester.assertImpresiceEquals(Utils.inchesToMeters(2716.53), testDouble, "Inches to meters test");
        Tester.assertEquals(Utils.last(testArray), 11.45, "Utils.last test");
        Tester.assertEquals(Utils.signOf(testDouble), 1, "testDouble test");
        Utils.trimIf(comparableArray,100);
        Tester.assertEquals(testArray, comparableArray, "trimIf test");
        Tester.assertTrue(Utils.inRange(100,1,1000), "inRange test #1");
        Tester.assertTrue(Utils.inRange(testArray, 1000), "inRange test #2");
        Utils.trimAdd(comparableArray, 4.0, 4);
        Tester.assertNotEquals(testArray, comparableArray, "trimAdd test");
        comparableArray.remove(3);
        Tester.assertEquals(Utils.limitOutput(100, 1), 1, "limitOutput test");
        Tester.assertEquals(Utils.boolToInt(true), 1, "boolToInt test");
        Tester.assertEquals(Utils.arrayListToHashset(stringArray), testHashSet, "arrayListToHashset test");
        Tester.assertEquals(Utils.combineArray(testArray.toArray(), comparableArray.toArray())[3], combinedArray.toArray()[3], "combineArray test"); // Compare third element of both
        
        Utils.deepCopy(arrayToCopy, newArray);
        Tester.assertEquals(newArray.get(1), 3, "deepCopy");

        Tester.assertTrue(Utils.oppositeDigitalOutput(false), "oppositeDigitalOutput, #1");
        
        Tester.assertEquals(Utils.cummSums(arrayToCopy).get(1), 5, "cummSums");
        Tester.assertEquals(Utils.cummSums(arrayToCopy).get(2), 9, "cummSums");

        Tester.assertTrue (Utils.inBounds(4, bounds), "inBounds");
        Tester.assertFalse(Utils.inBounds(6, bounds), "inBounds");

        Tester.assertEquals(Utils.toArrayList(arrayToCopy).get(1), 3, "toArrayList, iterable");
        Tester.assertEquals(Utils.toArrayList(arr).get(1), 3, "toArrayList, array");
        Tester.assertEquals(Utils.toArrayList(streamList).get(2), 3, "toArrayList, stream");

        Tester.assertEquals(Utils.sumArrayList(arrayToCopy), 9, "sumArrayList");
    }
}