class pathfinder 
{
	int[][] matrix;
	boolean[][] visited;

	int w, h;

	int[][][] path;

	public pathfinder()
	{
		w = 18;
		h = 10;
		matrix = new int[][]{
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		    {0,1,1,0,1,1,0,1,1,0,1,1,0,1,1,0,1,1,0},
		    {0,1,1,0,1,1,0,1,1,0,1,1,0,1,1,0,0,0,0},
		    {0,0,0,0,1,1,0,1,1,0,1,1,0,1,1,0,1,1,0},
		    {0,1,1,0,1,1,0,1,1,0,1,1,0,1,1,0,0,0,0},
		    {0,1,1,0,1,1,0,1,1,0,1,1,0,1,1,0,1,1,0},
		    {0,0,0,0,1,1,0,1,1,0,1,1,0,1,1,0,0,0,0},
		    {0,1,1,0,1,1,0,1,1,0,1,1,0,1,1,0,0,0,0},
		    {0,1,1,0,1,1,0,1,1,0,1,1,0,1,1,0,0,0,0},
		    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
		};

		visited = new boolean[h][w];
    	for (int i = 0; i < h; i++)
    		for (int k = 0; k < w; k++)
    			visited[i][k] = false;
	}
	
	public int[][] calcDist(int x, int y) {
		int[][] distance = new int[h][w];
		for (int i = 0; i < h; i++)
			for (int k = 0; k < w; k++)
				distance[i][k] = -1;
		distance[0][0] = 0;

		boolean done = false;
		int iter = 0;
		int cooldown = 2;
		while (!done) {
			iter++;
			if (iter > 1001) break;
			boolean found = false;
			for (int i = 0; i < h; i++) {
				for (int k = 0; k < w; k++) {
					//System.out.println("Process " + i + " " + k);
					int curr = distance[i][k];
					if (curr != -1) {
						found = true;
						if (i > 0 && 
							(
								(distance[i - 1][k] == -1) ||
								(distance[i - 1][k] > (curr + 1))
							) &&
							matrix[i - 1][k] == 0)
						{
							distance[i - 1][k] = curr + 1;
						}
						if (i < (h - 1) &&
							(
								(distance[i + 1][k] == -1) ||
								(distance[i + 1][k] > (curr + 1))
							) &&
							matrix[i + 1][k] == 0)
						{
							distance[i + 1][k] = curr + 1;
						}
						if (k > 0 &&
							(
								(distance[i][k - 1] == -1) ||
								(distance[i][k - 1] > (curr + 1))
							) &&
							matrix[i][k - 1] == 0)
						{
							distance[i][k - 1] = curr + 1;
						}
						if (k < (w - 1) &&
							(
								(distance[i][k + 1] == -1) || 
								(distance[i][k + 1] > (curr + 1))
							) &&
							matrix[i][k + 1] == 0)
						{
							distance[i][k + 1] = curr + 1;
						}
					}
				}
			}
			if (!found) cooldown--;
			if (cooldown == 0)done = true;
		}
		return distance;
		/*
		for (int i = 0; i < h; i++) {
			for (int k = 0; k < w; k++) {
				
				if (distance[i][k] == -1)
					System.out.print("  ");
				else
					System.out.print(distance[i][k] + " ");
			}
			System.out.print('\n');
		}
		*/
	}
}