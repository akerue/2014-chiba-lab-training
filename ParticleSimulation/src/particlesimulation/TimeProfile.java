package particlesimulation;

public class TimeProfile {
	private static long start_time = 0;

	public static void start(){
		start_time = System.nanoTime();
	}

	public static long nano_time() {
		return System.nanoTime() - start_time;
	}

	public static long micro_time() {
		return (System.nanoTime() - start_time) / 1000;
	}
	
	public static long milli_time() {
		return (System.nanoTime() - start_time) / 10000000;
	}

	public static void print_nano_time(){
		System.out.println(nano_time());
	}
	
	public static void print_nano_time(String msg){
		System.out.println(msg + nano_time());
	}

	public static void print_micro_time(){
		System.out.println(micro_time());
	}

	public static void print_micro_time(String msg){
		System.out.println(msg + micro_time());
	}

	public static void print_milli_time(){
		System.out.println(milli_time());
	}

	public static void print_milli_time(String msg){
		System.out.println(msg + milli_time());
	}
}
