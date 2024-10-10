package acount.poly.minhkt_imtatech_duan.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import acount.poly.minhkt_imtatech_duan.Dao.LuuHD_Dao;
import acount.poly.minhkt_imtatech_duan.Model.LuuHoaDon;
import acount.poly.minhkt_imtatech_duan.R;

import java.util.ArrayList;

public class ThongKeDT_Adapter extends RecyclerView.Adapter<ThongKeDT_Adapter.ViewHolder>{

    ArrayList<LuuHoaDon> listHoaDon;
    Context context;
    LuuHD_Dao luuHDDAO;

    public ThongKeDT_Adapter(Context context, ArrayList<LuuHoaDon> listHoaDon){
        this.context = context;
        this.listHoaDon = listHoaDon;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thongkedoanhthu, parent, false);
        luuHDDAO = new LuuHD_Dao(view.getContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LuuHoaDon hoaDon = listHoaDon.get(position);
        holder.txtTkdtTenSP.setText(hoaDon.getTenKhachHang());
        double doanhThu = hoaDon.getThanhTien();
        String outDoanhThu = String.format("%,.0f", doanhThu);
        String subDoanhThu = outDoanhThu.substring(0, (outDoanhThu.length() - 4));
        holder.txtTkdtThanhTien.setText(subDoanhThu + "K VNĐ");

        if (position == (listHoaDon.size() - 1)){
            holder.bottomViewTkdt.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Tạo dialog
                Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog_thanh_toan);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//                    Ánh xạ
                TextView txtHoaDonTitle = dialog.findViewById(R.id.txtHoaDonTitle);
                EditText btnHoaDonHuy = dialog.findViewById(R.id.btnHoaDonHuy);
                EditText btnHoaDonXN = dialog.findViewById(R.id.btnHoaDonXN);

                TextView txtHDTenNV = dialog.findViewById(R.id.txtHDTenNV);
                TextView txtHDTenKH = dialog.findViewById(R.id.txtHDTenKH);

                TextView txtHDDiaChi = dialog.findViewById(R.id.txtHDDiaChi);

                TextView txtHDNgayBan = dialog.findViewById(R.id.txtHDNgayBan);

                TextView txtHDTrangThai = dialog.findViewById(R.id.txtHDTrangThai);

                RecyclerView recycle_hoaDon = dialog.findViewById(R.id.recycle_hoaDon);
                TextView txtHDTongTien = dialog.findViewById(R.id.txtHDTongTien);

                ArrayList<LuuHoaDon> listHoaDon2 = luuHDDAO.getHDofMaHD(hoaDon.getMaHoaDon());

                txtHoaDonTitle.setText("Chi tiết hóa đơn");
                txtHDTenNV.setText(listHoaDon2.get(0).getTenUser());
                txtHDTenKH.setText(listHoaDon2.get(0).getTenKhachHang());

                txtHDNgayBan.setText(listHoaDon2.get(0).getNgayLapHD());
                txtHDDiaChi.setText(listHoaDon2.get(0).getDiaChi());
                txtHDTrangThai.setText(listHoaDon2.get(0).getTrangThai());

                txtHDTrangThai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog1 = new Dialog(v.getContext());
                        dialog1.setContentView(R.layout.dialog_update_trangthai);
                        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        dialog1.show();

                        EditText btnUpdateTT = dialog1.findViewById(R.id.btnUpdateTrangThai);
                        EditText btnHuyTT = dialog1.findViewById(R.id.btnHuyTrangThai);
                        EditText edtUpdateTrangThai = dialog1.findViewById(R.id.edtUpdateTrangThai);

                        btnHuyTT.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                            }
                        });
                        btnUpdateTT.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String trangthai = edtUpdateTrangThai.getText().toString();
                                hoaDon.setTrangThai(trangthai);

                                boolean isSuccess = luuHDDAO.updateTrangThai(hoaDon);
                                if (isSuccess) {
                                    listHoaDon2.clear();
                                    listHoaDon2.addAll(luuHDDAO.getHDofMaHD(hoaDon.getMaHoaDon()));
                                    txtHDTrangThai.setText(listHoaDon2.get(0).getTrangThai());
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
                                    dialog1.dismiss();
                                } else {
                                    Toast.makeText(context, "Cập nhật trạng thái thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                });

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(dialog.getContext());
                recycle_hoaDon.setLayoutManager(linearLayoutManager);
                ThongTinHD_Adapter thongTinHDAdapter = new ThongTinHD_Adapter(dialog.getContext(), listHoaDon2);
                recycle_hoaDon.setAdapter(thongTinHDAdapter);

                double doanhThu = luuHDDAO.tongThuHD(hoaDon.getMaHoaDon());
                String outDoanhThu = String.format("%,.0f", doanhThu);
                txtHDTongTien.setText(outDoanhThu + " VNĐ");


                btnHoaDonXN.setVisibility(View.GONE);
                btnHoaDonHuy.setText("Đóng");
                btnHoaDonHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    public void reloadData() {
        // Load lại dữ liệu, có thể thông qua việc gọi notifyDataSetChanged() hoặc một phương thức tương tự.
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listHoaDon.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTkdtTenSP, txtTkdtThanhTien;
        View bottomViewTkdt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTkdtTenSP = itemView.findViewById(R.id.txtTkdtTenSP);
            txtTkdtThanhTien = itemView.findViewById(R.id.txtTkdtThanhTien);
            bottomViewTkdt = itemView.findViewById(R.id.bottomViewTkdt);
        }
    }
}

