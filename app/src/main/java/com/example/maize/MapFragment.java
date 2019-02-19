package com.example.maize;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    int rowNum, colNum;
    int[][] mapMatrix;

    public int[][] calcDist(int x, int y) {
        int[][] distance = new int[rowNum][colNum];
        for(int i = 0; i < rowNum; i++)
            for (int k = 0; k < colNum; k++)
                distance[i][k] = -1;
        distance[x][y] = 0;

        boolean done = false;
        int iter = 0;
        int cooldown = 2;
        while(!done) {
            iter++;
            if (iter > 1000) break;
            boolean found = false;
            for (int i = 0; i < rowNum; i++) {
                for (int k = 0; k < colNum; k++) {
                    int curr = distance[i][k];
                    if (curr != -1) {
                        found = true;
                        if (i > 0 &&
                            (
                                (distance[i - 1][k] == -1) ||
                                (distance[i - 1][k] > (curr + 1))
                            ) &&
                            mapMatrix[i - 1][k] == 0) {
                            distance[i - 1][k] = curr + 1;
                        }
                        if (i < (rowNum - 1) &&
                            (
                                (distance[i + 1][k] == -1) ||
                                (distance[i + 1][k] > (curr + 1))
                            ) &&
                            mapMatrix[i + 1][k] == 0) {
                            distance[i + 1][k] = curr + 1;
                        }
                        if (k > 0 &&
                            (
                                (distance[i][k - 1] == -1) ||
                                (distance[i][k - 1] > (curr + 1))
                            ) &&
                            mapMatrix[i][k - 1] == 0) {
                            distance[i][k - 1] = curr + 1;
                        }
                        if (k < (colNum - 1) &&
                            (
                                (distance[i][k + 1] == -1) ||
                                (distance[i][k + 1] > (curr + 1))
                            ) &&
                            mapMatrix[i][k + 1] == 0) {
                            distance[i][k + 1] = curr + 1;
                        }
                    }
                }
            }
            if (!found) cooldown--;
            if (cooldown == 0) done = true;
        }

        for (int i = 0; i < rowNum; i++) {
            for (int k = 0; k < colNum; k++) {
                if (distance[i][k] == -1)
                    System.out.print("  ");
                else
                    System.out.print(distance[i][k] + " ");
            }
            System.out.print("\n");
        }

        return distance;
    }

    /*@Override
    public boolean onTouch(View view, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        System.out.println(x + " " + y);
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ImageView map = (ImageView) getView().findViewById(R.id.map);
                int[] mapLoc = new int[2];
                map.getLocationInWindow(mapLoc);
                System.out.println("Touching: " + mapLoc[0] + ", " + mapLoc[1]);
        }
        return false;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Server Connecton
        JSONObject jsonRes;
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "http://piyo.cafe:4600/api/map";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String res) {
                        System.out.println("Response" + res);
                        Log.d("Response", res);
                        try {
                            JSONObject jsonObj = new JSONObject(res);

                            //canvas.drawRect(new RectF(0, 0, 1000, 1900), white);
                            //canvas.drawRect(new RectF(45, 45, 55, 1855), black);
                            //canvas.drawRect(new RectF(45, 45, 955, 55), black);

                            JSONArray rows = jsonObj.getJSONArray("map");
                            rowNum = rows.length();
                            colNum = jsonObj.getJSONArray("map").getJSONArray(0).length();
                            mapMatrix = new int[rowNum][colNum];

                            for (int i = 0; i < rowNum; i++) {
                                JSONArray cols = rows.getJSONArray(i);
                                for (int k = 0; k < colNum; k++) {
                                    mapMatrix[i][k] = cols.getInt(k);
                                }
                            }
                            System.out.println(mapMatrix[1][1]);

                            int[][] entrances;
                            int[][] exits;

                            JSONArray entranceArr = jsonObj.getJSONObject("locations").getJSONArray("entrances");
                            JSONArray exitsArr = jsonObj.getJSONObject("locations").getJSONArray("exits");

                            entrances = new int[entranceArr.length()][2];
                            exits = new int[exitsArr.length()][2];

                            System.out.println("Entrance: " + entranceArr.length() + " | Exits: " + exitsArr.length());
                            for (int i = 0; i < entranceArr.length(); i++) {
                                JSONObject elem = entranceArr.getJSONObject(i);
                                entrances[i][0] = elem.getInt("x");
                                entrances[i][1] = elem.getInt("y");
                            }
                            for (int i = 0; i < exitsArr.length(); i++) {
                                JSONObject elem = exitsArr.getJSONObject(i);
                                exits[i][0] = elem.getInt("x");
                                exits[i][1] = elem.getInt("y");
                            }

                            // Map Drawable
                            ImageView map = (ImageView) getView().findViewById(R.id.map);

                            map.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent e) {
                                    int x = (int) e.getX() / 100;
                                    int y = (int) e.getY() / 100;

                                    StoreLayout activity = (StoreLayout) getActivity();
                                    int startc = y;
                                    int startr = 9 - x;

                                    System.out.println(activity.startr + ", " + activity.startc);
                                    System.out.println(x + ", " + y);

                                    if (
                                        (startr == 4 && startc == 0) ||
                                        (startr == 0 && startc == 9) ||
                                        (startr == 9 && startc == 17)
                                    ) {
                                        activity.startr = startr;
                                        activity.startc = startc;

                                        Toast.makeText(activity, "Updated the entrance.", Toast.LENGTH_SHORT).show();
                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
                                    }
                                    return false;
                                }
                            });

                            Bitmap mapBitmap = Bitmap.createBitmap(rowNum * 100, colNum * 100, Bitmap.Config.RGB_565);
                            Canvas canvas = new Canvas(mapBitmap);
                            Paint black = new Paint();
                            black.setARGB(155, 15, 15, 15);
                            Paint white = new Paint();
                            white.setARGB(255, 255, 255, 255);
                            Paint highlight = new Paint();
                            highlight.setARGB(255, 27, 216, 181);
                            Paint exit = new Paint();
                            exit.setARGB(255, 200, 0, 0);
                            Paint itemColor = new Paint();
                            itemColor.setARGB(255,216, 181, 27);

                            System.out.println("Row: " + rowNum + " | Col: " + colNum);
                            for (int i = 0; i < rowNum; i++) {
                                for (int k = 0; k < colNum; k++) {
                                    System.out.print(mapMatrix[i][k] + " ");
                                }
                                System.out.print("\n");
                            }

                            canvas.drawRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), white);
                            for (int i = 0; i < rowNum; i++) {
                                for (int k = 0; k < colNum; k++) {
                                    if (mapMatrix[i][k] == 1)
                                        canvas.drawRect(new RectF(canvas.getWidth() + (i * -100), k * 100, canvas.getWidth() + (i * -100) - 100, (k * 100) + 100), black);
                                }
                            }

                            canvas.drawRect(new RectF(0, 0, 10, canvas.getHeight()), black);
                            canvas.drawRect(new RectF(0, 0, canvas.getWidth(), 10), black);
                            canvas.drawRect(new RectF(canvas.getWidth() - 10, 0, canvas.getWidth(), canvas.getHeight()), black);
                            canvas.drawRect(new RectF(0, canvas.getHeight() - 10, canvas.getWidth(), canvas.getHeight()), black);

                            for (int i = 0; i < entrances.length; i++) {
                                int x = entrances[i][0];
                                int y = entrances[i][1];

                                if (y == 0)
                                    canvas.drawRect(
                                            new RectF(
                                                    canvas.getWidth() + (x * -100),
                                                    0,
                                                    canvas.getWidth() + (x * -100) - 100,
                                                    15
                                            ),
                                            highlight
                                    );
                                if (x == 0)
                                    canvas.drawRect(
                                            new RectF(
                                                    canvas.getWidth(),
                                                    (y * 100),
                                                    canvas.getWidth() - 15,
                                                    (y * 100) + 100
                                            ),
                                            highlight
                                    );
                                if (y == colNum - 1)
                                    canvas.drawRect(
                                            new RectF(
                                                    canvas.getWidth() + (x * -100),
                                                    canvas.getHeight() - 15,
                                                    canvas.getWidth() + (x * -100) - 100,
                                                    canvas.getHeight()
                                            ),
                                            highlight
                                    );
                                if (x == rowNum - 1)
                                    canvas.drawRect(
                                            new RectF(
                                                    0,
                                                    (y * 100),
                                                    15,
                                                    (y * 100) + 100
                                            ),
                                            highlight
                                    );

                                canvas.drawRect(new RectF(canvas.getWidth() + (x * -100) - 25, y * 100 + 25, canvas.getWidth() + (x * -100) - 75, (y * 100) + 75), highlight);
                            }

                            for (int i = 0; i < exits.length; i++) {
                                int x = exits[i][0];
                                int y = exits[i][1];

                                if (y == 0)
                                    canvas.drawRect(
                                            new RectF(
                                                    canvas.getWidth() + (x * -100),
                                                    0,
                                                    canvas.getWidth() + (x * -100) - 100,
                                                    15
                                            ),
                                            highlight
                                    );
                                if (x == 0)
                                    canvas.drawRect(
                                            new RectF(
                                                    canvas.getWidth(),
                                                    (y * 100),
                                                    canvas.getWidth() - 15,
                                                    (y * 100) + 100
                                            ),
                                            exit
                                    );
                                if (y == colNum - 1)
                                    canvas.drawRect(
                                            new RectF(
                                                    canvas.getWidth() + (x * -100),
                                                    canvas.getHeight() - 15,
                                                    canvas.getWidth() + (x * -100) - 100,
                                                    canvas.getHeight()
                                            ),
                                            exit
                                    );
                                if (x == rowNum - 1)
                                    canvas.drawRect(
                                            new RectF(
                                                    0,
                                                    (y * 100),
                                                    15,
                                                    (y * 100) + 100
                                            ),
                                            exit
                                    );

                                canvas.drawRect(new RectF(canvas.getWidth() + (x * -100) - 25, y * 100 + 25, canvas.getWidth() + (x * -100) - 75, (y * 100) + 75), exit);
                            }

                            StoreLayout activity = (StoreLayout) getActivity();
                            if (activity.cart.size() > 0) {
                                for (int i = 0; i < activity.cart.size(); i++) {
                                    ShopItem elem = activity.cart.get(i);
                                    int x = elem.getXa();
                                    int y = elem.getYa();
                                    canvas.drawRect(new RectF(
                                            canvas.getWidth() + (x * -100) - 25,
                                            y * 100 + 25,
                                            canvas.getWidth() + (x * -100) - 75,
                                            y * 100 + 75
                                    ), itemColor);
                                }

                                ArrayList<ShopItem> processQueue = new ArrayList<>(activity.cart);
                                ArrayList<Integer> cost = new ArrayList<Integer>(0);
                                ArrayList<ShopItem> order = new ArrayList<ShopItem>(0);
                                int r = 2;
                                int c = 0;
                                while (processQueue.size() > 0) {
                                    ShopItem elem = processQueue.remove(0);
                                    // start location
                                    int steps = calcDist(elem.getXa(), elem.getYa())[r][c];

                                    int index = 0;
                                    for (int i = 0; i <= order.size(); i++) {
                                        index = i;
                                        if (index != order.size() && steps > cost.get(i)) break;
                                    }

                                    System.out.println("Steps: " + steps);
                                    cost.add(index, steps);
                                    order.add(index, elem);
                                    System.out.println("Ordered:");
                                    for (int i = 0; i < cost.size(); i++) {
                                        System.out.print("(" + cost.get(i) + ", " + order.get(i).getName() + ") ");
                                    }
                                    System.out.print("\n");
                                }

                                r = activity.startr;
                                c = activity.startc;
                                order.add(0, new ShopItem("Exit", 3, 15, 3, 15, "", -1));
                                while (order.size() > 0) {
                                    ShopItem elem = order.remove(order.size() - 1);
                                    System.out.println("Name: " + elem.getName());
                                    int[][] gradient = calcDist(elem.getXa(), elem.getYa());
                                    int maxSteps = gradient[r][c];

                                    int currSteps = maxSteps;
                                    canvas.drawCircle(canvas.getWidth() - (100 * r) - 50, (100 * c) + 50, 10, black);
                                    for (int i = 0; i < maxSteps + 1; i++) {
                                        int ra = r;
                                        int ca = c;
                                        // up
                                        if (r > 0 && gradient[r - 1][c] < currSteps && gradient[r -1][c] != -1) {
                                            r--;
                                        }
                                        // down
                                        else if (r < rowNum - 1 && gradient[r + 1][c] < currSteps && gradient[r + 1][c] != -1) {
                                            r++;
                                        }
                                        // left
                                        else if (c > 0 && gradient[r][c - 1] < currSteps && gradient[r][c - 1] != -1) {
                                            c--;
                                        }
                                        // right
                                        else if (c < colNum - 1 && gradient[r][c + 1] < currSteps && gradient[r][c + 1] != -1) {
                                            c++;
                                        }
                                        currSteps = gradient[r][c];

                                        canvas.drawCircle(canvas.getWidth() - (100 * r) - 50, (100 * c) + 50, 10, black);

                                        System.out.println("Moved to: ( " + c + ", " + r + " )");
                                    }
                                }
                            }

                            map.setImageDrawable(new BitmapDrawable(getResources(), mapBitmap));
                        }
                        catch (Throwable tx) {

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        System.out.println("Error:" + err);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("key", "val");

                return params;
            }
        };
        queue.add(postRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_map, container, false);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
