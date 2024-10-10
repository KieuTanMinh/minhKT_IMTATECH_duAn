package acount.poly.minhkt_imtatech_duan.Adapter;

import  android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import acount.poly.minhkt_imtatech_duan.Dao.LuuHD_Dao;
import acount.poly.minhkt_imtatech_duan.Dao.User_Dao;
import acount.poly.minhkt_imtatech_duan.Fragment.ViewUserInfor_Frg;
import acount.poly.minhkt_imtatech_duan.Model.LuuHoaDon;
import acount.poly.minhkt_imtatech_duan.Model.User;
import acount.poly.minhkt_imtatech_duan.R;

import java.util.ArrayList;

public class ThongKeNV_Adapter extends RecyclerView.Adapter<ThongKeNV_Adapter.ViewHolder>{

    ArrayList<LuuHoaDon> list;
    Context context;
    LuuHD_Dao luuHDDAO;
    User_Dao userDAO;

    public ThongKeNV_Adapter(Context context, ArrayList<LuuHoaDon> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thongkenhanvien, parent, false);
        luuHDDAO = new LuuHD_Dao(view.getContext());
        userDAO = new User_Dao(view.getContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        Settext

        LuuHoaDon luuHoaDon = list.get(position);
        String index = "";
        if (position < 9){
            index = "0" + (position + 1);
        }
        else{
            index = String.valueOf(position + 1);
        }
        holder.txtTknvSTT.setText(index);
        holder.txtTknvTenNv.setText(luuHoaDon.getTenUser());

        if (String.valueOf(luuHoaDon.getThanhTien()) != null){
            double doanhThu = luuHoaDon.getThanhTien();
            String outTongTien = String.format("%,.0f", doanhThu);
            if (outTongTien.length() > 4){
                String subDoanhThu = outTongTien.substring(0, (outTongTien.length() - 4));
                holder.txtTknvDoanhThu.setText(subDoanhThu + "K VNĐ");
            }
            else {
                holder.txtTknvDoanhThu.setText(outTongTien + "K VNĐ");
            }

        }
        else {
            holder.txtTknvDoanhThu.setText("0 VNĐ");
        }
        if (position == (list.size() - 1)){
            holder.bottomViewTknv.setVisibility(View.GONE);
        }

        User user = userDAO.getUser(luuHoaDon.getMaUser());

//        Item Click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Load Fragment hiển thị thông tin nhân viên
                loadFragment(new ViewUserInfor_Frg(user));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTknvSTT, txtTknvTenNv, txtTknvDoanhThu;
        View bottomViewTknv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTknvSTT = itemView.findViewById(R.id.txtTknvSTT);
            txtTknvTenNv = itemView.findViewById(R.id.txtTknvTenNv);
            txtTknvDoanhThu = itemView.findViewById(R.id.txtTknvDoanhThu);
            bottomViewTknv = itemView.findViewById(R.id.bottomViewTknv);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

