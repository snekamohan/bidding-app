package com.example.auctionapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.adapter.ProductAdapter;
import com.example.auctionapp.model.productModel;
import com.example.auctionapp.model.userModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProducts extends AppCompatActivity {

    private long pressedTime;

    private RecyclerView rv_showAllFood;
    private ProductAdapter adapter;
    private List<productModel> mList = new ArrayList<>();

    private FloatingActionButton fab_createFood;
    private String username = "";
    private String phoneNumber = "";

    private ProgressDialog progressDialog;

    private static final int STORAGE_PERMISSION_CODE = 101;
    StorageReference storageReference;
    public static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String imageString;
    private StorageTask uploadTask;

    DatabaseReference reference;
    FirebaseUser firebaseUser;
    CircleImageView iv_foodImage;

    ImageView iv_showOrders;
    ImageView iv_logout;

    public MyProducts() {
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);

        rv_showAllFood = findViewById(R.id.rv_showAllFood);
        rv_showAllFood.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyProducts.this);
        linearLayoutManager.setStackFromEnd(true);
        rv_showAllFood.setLayoutManager(linearLayoutManager);
        fab_createFood = findViewById(R.id.fab_createFood);

        iv_logout = findViewById(R.id.iv_logout);
        iv_showOrders = findViewById(R.id.iv_showOrders);

        iv_showOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProducts.this, ShowAllBids.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MyProducts.this, UserSignin.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                MyProducts.this.finish();
            }
        });

        progressDialog = new ProgressDialog(MyProducts.this);

        // getting restaurant name from firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(UserRegister.RIDER_USERS).child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel user = snapshot.getValue(userModel.class);
                username = user.getUsername();
                phoneNumber = user.getPhoneNumber();
                getAllFood(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fab_createFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(username, phoneNumber);
            }
        });

    }

    private void createDialog(String username, String phoneNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyProducts.this);
        View view = LayoutInflater.from(MyProducts.this).inflate(R.layout.add_product, null, false);
        builder.setView(view);
        EditText et_foodName = view.findViewById(R.id.et_foodName);
        EditText et_foodDesc = view.findViewById(R.id.et_foodDesc);
        EditText et_foodPrice = view.findViewById(R.id.et_foodPrice);
        iv_foodImage = view.findViewById(R.id.iv_foodImage);
        Button btn_addFood = view.findViewById(R.id.btn_addFood);
        AlertDialog alertDialog = builder.create();

        storageReference = FirebaseStorage.getInstance().getReference().child("Uploads");

        iv_foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
            }
        });
        alertDialog.show();
        btn_addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = et_foodName.getText().toString();
                String foodDesc = et_foodDesc.getText().toString();
                String foodPrice = et_foodPrice.getText().toString();
                String foodImage = imageString;
                if (foodName.isEmpty()) {
                    et_foodName.setError("Empty Name");
                } else if (foodDesc.isEmpty()) {
                    et_foodDesc.setError("Empty Desc");
                } else if (foodPrice.isEmpty()) {
                    et_foodPrice.setError("Empty Desc");
                } else {
                    progressDialog.setMessage("Adding Your Food");
                    progressDialog.setTitle("Adding...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    createFood(foodName, foodDesc, foodPrice, username, foodImage, phoneNumber);
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void createFood(String foodName, String foodDesc, String foodPrice, String username, String foodImage, String phoneNumber) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(UserRegister.RESTAURANT_FOOD).child(username);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("foodName", foodName);
        hashMap.put("foodDesc", foodDesc);
        hashMap.put("foodPrice", foodPrice);
        hashMap.put("foodImage", foodImage);
        hashMap.put("phoneNumber", phoneNumber);
        hashMap.put("username", username);
        hashMap.put("id", userId);
        reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    getAllFood(username);
                    Toast.makeText(MyProducts.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                    imageString = "";
                } else {
                    Toast.makeText(MyProducts.this, "Product Created Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = MyProducts.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(MyProducts.this);
        pd.setMessage("Uploading...");
        pd.show();
        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        try {
                            Uri downloadingUri = task.getResult();
                            Log.d("TAG", "onComplete: uri completed");
                            String mUri = downloadingUri.toString();
                            imageString = mUri;
                            Glide.with(MyProducts.this).load(imageUri).into(iv_foodImage);
                        } catch (Exception e) {
                            Log.d("TAG1", "error Message: " + e.getMessage());
                            Toast.makeText(MyProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        pd.dismiss();
                    } else {
                        Toast.makeText(MyProducts.this, "Failed here", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MyProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(MyProducts.this, "No image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(MyProducts.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }

    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MyProducts.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MyProducts.this, new String[]{permission}, requestCode);
        } else {
            openImage();
            Toast.makeText(MyProducts.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImage();
                Toast.makeText(MyProducts.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MyProducts.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getAllFood(String username) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.getUid() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(UserRegister.RESTAURANT_FOOD).child(username);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        productModel restaurantFood = dataSnapshot.getValue(productModel.class);
                        mList.add(restaurantFood);
                    }
                    adapter = new ProductAdapter(MyProducts.this, mList);
                    rv_showAllFood.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

}