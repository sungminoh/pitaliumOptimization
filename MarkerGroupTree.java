import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;

public class MarkerGroupTree {
	public static class Node{
		public int x;
		public int y;
		public Node parent;
		public Node left;
		public Node right;
		public HashSet<Node> group;

		public Node(int x, int y){
			this.x = x;
			this.y = y;
			group = new HashSet<Node>();
			group.add(this);
		}

		public Node(Point p){
			this.x = p.x;
			this.y = p.y;
			group = new HashSet<Node>();
			group.add(this);
		}
	}

	private static final int GROUP_DISTANCE = 10;
	public Node root;
	public HashMap<HashSet<Node>, int[]> groups;

	public MarkerGroupTree(){
		root = null;
		groups = new HashMap<HashSet<Node>, int[]>();
	}

	// time complexity : O(nlogn)
	public void insert(Point p){
		Node node = new Node(p);
		int[] nodeMinMax = new int[]{node.x, node.y, node.x, node.y};
		groups.put(node.group, nodeMinMax);

		if(root == null){
			root = node;
		}
		else{
			Node pointer = root;
			Node pointer_parent = root.parent;
			while(pointer != null){
				// If the node is close to the other node, those are in same group.
				if(Math.abs(node.x - pointer.x) < GROUP_DISTANCE && Math.abs(node.y - pointer.y) < GROUP_DISTANCE && node.group != pointer.group){
					int[] pointerMinMax = groups.get(pointer.group);
					int[] updateMinMax = updateMinMax(nodeMinMax, pointerMinMax);
					groups.remove(pointer.group);
					groups.remove(node.group);
					node.group.addAll(pointer.group);
					HashSet<Node> group = pointer.group;
					for(Node element : group){
						element.group = node.group;
					};
					
					groups.put(node.group, updateMinMax);
				}
				pointer_parent = pointer;
				pointer = (node.x < pointer.x) ? pointer.left : pointer.right;
			}

			if(node.x < pointer_parent.x){
				pointer_parent.left = node;
			}else{
				pointer_parent.right = node;
			}
			node.parent = pointer_parent;
		}
	}
	public int[] updateMinMax(int[] nodeMinMax, int[] pointerMinMax){
		if(pointerMinMax[0] < nodeMinMax[0]){ nodeMinMax[0] = pointerMinMax[0]; }
		if(pointerMinMax[1] < nodeMinMax[1]){ nodeMinMax[1] = pointerMinMax[1]; }
		if(pointerMinMax[2] > nodeMinMax[2]){ nodeMinMax[2] = pointerMinMax[2]; }
		if(pointerMinMax[3] > nodeMinMax[3]){ nodeMinMax[3] = pointerMinMax[3]; }
		return nodeMinMax;
	}
	public void print_map(HashMap<HashSet<Node>, int[]> map){
		System.out.printf("{");
		for(HashSet<Node> set : map.keySet()){
			print_set(set);
			System.out.printf(": ");
			print_array(map.get(set));

		}
		System.out.printf("} ");
	}
	public void print_array(int[] arr){
		System.out.printf("|");
		//            if(arr == null){ System.out.println("array is null");}
		//            else{ System.out.printf("array length is %d\n", arr.length);}
		for(int i=0; i<arr.length; i++){
			System.out.printf("%d ", arr[i]);
		}
		System.out.printf("|");
	}
	public void print_set(HashSet<Node> set){
		System.out.printf("[");
		for(Node node : set){
			System.out.printf("(%d, %d)", node.x, node.y);
		}
		System.out.printf("]");
	}
	public void insert(List<Point> pList){
		for(Point p : pList){
			insert(p);
		}
		print_map(groups);
		System.out.println();
	}

	public List<Rectangle> getAreas(){
		List<Rectangle> ret = new ArrayList<Rectangle>();
		int i = 0;
		for(HashSet<Node> group : groups.keySet()){
			int[] minMax = groups.get(group);
			System.out.printf("%d-th group: %d %d %d %d\n", i++, minMax[0], minMax[1], minMax[2], minMax[3]);
			Rectangle rec = new Rectangle(minMax[0]-(GROUP_DISTANCE/2), minMax[3]-(GROUP_DISTANCE/2), minMax[2] - minMax[0] + GROUP_DISTANCE, minMax[3] - minMax[1] + GROUP_DISTANCE);
			ret.add(rec);
		}
		return ret;
	}
}
