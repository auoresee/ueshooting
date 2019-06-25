package ueshooting.io;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FileLoader {
	public static ArrayList<String> loadStrings(String filename) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		ArrayList<String> lines = new ArrayList<String>();
		String temp;
		while(true){
			temp = br.readLine();
			if(temp == null)break;
			lines.add(temp);
		}
		br.close();
		return lines;
	}
	
	public static byte[] loadBytes(String filename) throws IOException{
		Path path = (new File(filename)).toPath();
		return Files.readAllBytes(path);
	}
}
