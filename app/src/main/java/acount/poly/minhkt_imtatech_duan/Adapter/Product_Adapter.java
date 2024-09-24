package acount.poly.minhkt_imtatech_duan.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import acount.poly.minhkt_imtatech_duan.Dao.GioHang_Dao;
import acount.poly.minhkt_imtatech_duan.Dao.User_Dao;
import acount.poly.minhkt_imtatech_duan.Fragment.ChiTietSP_Frg;
import acount.poly.minhkt_imtatech_duan.Fragment.SuaChiTietSP_Frg;
import acount.poly.minhkt_imtatech_duan.Model.GioHang;
import acount.poly.minhkt_imtatech_duan.Model.SanPham;
import acount.poly.minhkt_imtatech_duan.Model.User;
import acount.poly.minhkt_imtatech_duan.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Product_Adapter extends RecyclerView.Adapter<Product_Adapter.UserViewHolder> {

    private Context context;
    private ArrayList<SanPham> list;
    GioHang_Dao gioHangDAO;
    User_Dao userDAO;
    BottomNavigationView bottomNavigationView;

    public Product_Adapter(Context context, ArrayList<SanPham> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_sanpham, parent, false);
        gioHangDAO = new GioHang_Dao(view.getContext());
        userDAO = new User_Dao(view.getContext());
        bottomNavigationView = view.findViewById(R.id.navigation);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
//        Set Data cho List Item
        SanPham sanPham = list.get(position);
        holder.TenSanPham.setText(sanPham.getTenSanPham());
        holder.GiaTien.setText(String.valueOf(sanPham.getPrice()));
        byte[] productsImage = sanPham.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(productsImage, 0, productsImage.length);
        holder.img_SanPham.setImageBitmap(bitmap);
        //
        String outTongTien = String.format("%,.0f", sanPham.getPrice());
        holder.GiaTien.setText(outTongTien + " VNĐ");

//        Set sự kiện Onclick cho các Button
//        Buton xem sản phẩm
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bottomNavigationView.setSelectedItemId(R.id.pageBanHang);
                loadFragment(new ChiTietSP_Frg(sanPham));
            }
        });
//        Button thêm sản phẩm vào giỏ hàng (Icon ADD)
        holder.add_sanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Khởi tạo Model
                GioHang gioHang = new GioHang(1, sanPham.getId(), 1, "N", sanPham.getPrice());
//                Check Valid SP (SanPham.ID, Size)
                ArrayList<GioHang> outList = gioHangDAO.checkValidGioHang(gioHang);
//                Toast.makeText(context, outList.size() + "", Toast.LENGTH_SHORT).show();
                if (outList.size() != 0) {
//                - Có: Update số lượng
                    GioHang gioHang1 = outList.get(0);
//                    Toast.makeText(context, "OldSL: " + gioHang1.getSoLuong(), Toast.LENGTH_SHORT).show();
                    int newSL = gioHang1.getSoLuong() + 1;
//                    Toast.makeText(context, "NewSL" + newSL, Toast.LENGTH_SHORT).show();
                    gioHang.setSoLuong(newSL);
                    boolean kiemtra = gioHangDAO.updateGioHang(gioHang);
                    if (kiemtra) {
                        notifyDataSetChanged();
//                        Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Update SL Fail!", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                - Không: Thêm sản phẩm
                    boolean check = gioHangDAO.addGiohang(gioHang);
                    if (check) {
                        Toast.makeText(context, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Fail!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        SharedPreferences pref = context.getSharedPreferences("USER_FILE", context.MODE_PRIVATE);
        int maUser = pref.getInt("MA", 0);
        User user = userDAO.getUser(maUser);
        int quyenUser = user.getMaChucVu();
        if (quyenUser == 2) {
            holder.info_sanpham.setVisibility(View.GONE);
        }

        holder.info_sanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SuaChiTietSP_Frg(sanPham));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView add_sanpham, info_sanpham, img_SanPham;
        private TextView TenSanPham;
        private TextView GiaTien;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview_sanPham);
            info_sanpham = itemView.findViewById(R.id.info_sanpham);
            add_sanpham = itemView.findViewById(R.id.add_sanpham);
            img_SanPham = itemView.findViewById(R.id.img_sanpham);
            TenSanPham = itemView.findViewById(R.id.ten_sanpham);
            GiaTien = itemView.findViewById(R.id.gia_sanpham);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

