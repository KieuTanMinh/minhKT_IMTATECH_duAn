package acount.poly.minhkt_imtatech_duan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import acount.poly.minhkt_imtatech_duan.Fragment.ChiTietSP_Frg;
import acount.poly.minhkt_imtatech_duan.MainActivity;
import acount.poly.minhkt_imtatech_duan.Model.SanPham;
import acount.poly.minhkt_imtatech_duan.R;

import java.util.ArrayList;

public class Home_Adapter extends RecyclerView.Adapter<Home_Adapter.ViewHolder>{

    private ArrayList<SanPham> list;
    private Context context;

    public Home_Adapter(ArrayList<SanPham> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.cardview_sp_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sanPham = list.get(position);
        byte[] productsImage = sanPham.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(productsImage, 0, productsImage.length);
        holder.itemSpHomeImg.setImageBitmap(bitmap);
        holder.itemSpHomeTen.setText(sanPham.getTenSanPham());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.pageSanPham);
                loadFragment(new ChiTietSP_Frg(sanPham));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView itemSpHomeTen;
        ImageView itemSpHomeImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemSpHomeTen = itemView.findViewById(R.id.itemSpHomeTen);
            itemSpHomeImg = itemView.findViewById(R.id.itemSpHomeImg);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
