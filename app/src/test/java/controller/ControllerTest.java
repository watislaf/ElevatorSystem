package controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ControllerTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    public void should_create_controller_without_server() {
        // given
        Controller controller = new Controller();
        Thread t1 = new Thread(controller::start);
        // when
        // then
        Assertions.assertDoesNotThrow(t1::start);
        controller.stop();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void garbage() {
        Integer[] arr = {1, 2, 3};
        Integer[] arr2 = new Integer[4];
        //Arrays.stream(arr).parallel().isParallel();
        List<Integer> list1 = Stream.of(1, 2, 3).collect(Collectors.toCollection(LinkedList::new));
        List<Integer> list = Stream.of(1, 2, 3).toList();
        ArrayList<Integer> arrayList = new ArrayList<Integer>(list);
        ArrayList<Integer> arrayList2 = new ArrayList<Integer>();
        arrayList2.addAll(list);
        assert "5".equals("5");
        String input = "4212";

        String name = "baeldung";
        Optional<String> opt = Optional.of(name);

        String nullName = null;
        String name2 = Optional.ofNullable(nullName).orElse("john");
        String name3 = Optional.ofNullable(nullName).orElseGet(() -> "john");
        String name4 = Optional.ofNullable(nullName).orElseThrow();


        String password = " password ";
        Optional<String> passOpt = Optional.of(password);
        boolean correctPassword = passOpt
                .map(String::trim)
                .filter(pass -> pass.equals("password"))
                .isPresent();

        Callable<String> callableTask = () -> {
            TimeUnit.MILLISECONDS.sleep(300);
            return "Task's execution";
        };
        try {
            InputStreamReader reader = new InputStreamReader(System.in);
            BufferedReader reader2 = new BufferedReader(reader);
            reader2.readLine(); //  has a larger default buffer size / orces us to handle it/ synchronizes
            // is usually faster than Scanner because  it only reads the data without parsing it
            Scanner in = new Scanner(System.in);
            in.nextLine();
            in.nextInt();
            BufferedReader reader4 =
                    new BufferedReader(new FileReader("src/main/resources/input.txt"));

            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader2.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }
}