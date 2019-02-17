package com.example.maize;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShopFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private ListView mListView;
    private ArrayList<ShopItem> items;
    private CustomAdaptorItems customAdaptor;

    private OnFragmentInteractionListener mListener;

    public ShopFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ShopFragment newInstance() {
        ShopFragment fragment = new ShopFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, query);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_shop, container, false);

        if (getArguments() != null) {
            String query = getArguments().getString("query");
            System.out.println("Fragment: " + query);

            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url = "http://piyo.cafe:4600/api/search";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String res) {
                            System.out.println("Received result");
                            try {
                                JSONObject jsonObj = new JSONObject("{\"result\":" + res + "}");
                                System.out.println("Result: " + res);
                                System.out.println("JSON: " + jsonObj);

                                items = new ArrayList<ShopItem>();
                                //items.add(new ShopItem("a",0,0,0,0,"description1"));
                                //items.add(new ShopItem("b", 5,5,5,5,"description2", 0));

                                JSONArray jsonItems = jsonObj.getJSONArray("result");
                                for (int i = 0; i < jsonItems.length(); i++) {
                                    JSONObject item = jsonItems.getJSONObject(i);
                                    items.add(new ShopItem(
                                        item.getString("name") + " ($" + item.getDouble("price") + ")",
                                        item.getJSONObject("front").getInt("x"),
                                        item.getJSONObject("front").getInt("y"),
                                        item.getJSONObject("item").getDouble("x"),
                                        item.getJSONObject("item").getDouble("y"),
                                        item.getJSONObject("metadata").getString("description"),
                                        item.getInt("id")
                                    ));
                                }

                                mListView = view.findViewById(R.id.shoplist);
                                customAdaptor = new CustomAdaptorItems(view.getContext(), items);
                                mListView.setAdapter(customAdaptor);

                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        StoreLayout activity = (StoreLayout) getActivity();
                                        activity.cart.add(items.get(i));
                                        Toast.makeText(activity, "Added to cart.", Toast.LENGTH_SHORT).show();
                                        /*                startActivity(new Intent(StoreSelection.this, StoreLayout.class));*/
                                    }
                                });
                            }
                            catch (Throwable tx) {
                                System.out.println(tx.toString());
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
                    System.out.println("Param: " + getArguments().getString("query"));
                    params.put("query", getArguments().getString("query"));

                    return params;
                }
            };
            queue.add(postRequest);
        }

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
