package com.example.dentalcart.Repositories;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dentalcart.Pojo.CartModel;
import com.example.dentalcart.Pojo.ItemModel;
import com.example.dentalcart.Pojo.UserModel;
import com.example.dentalcart.UI.CartActivity;
import com.example.dentalcart.UI.LoginActivity;
import com.example.dentalcart.UI.MainActivity;
import com.example.dentalcart.UI.ShowproductActivity;
import com.example.dentalcart.UI.SplashscreenActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseOperations {
    private static final FirebaseAuth AUTH = FirebaseAuth.getInstance();
    private static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance() ;
    private static final DatabaseReference DATABASE_REFERENCE = FIREBASE_DATABASE.getReference() ;
    private static final FirebaseUser user = AUTH.getCurrentUser();

    // this method is used to register and then pass  your data to SaveUserAccount method
    public static void createAccount(UserModel users , ProgressDialog progressDialog , Context context){
        AUTH.createUserWithEmailAndPassword(users.getEmail() , users.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String userID = task.getResult().getUser().getUid() ;
                            saveUserAccount(users , userID , context , progressDialog) ;
                        }else {
                            Toast.makeText(context , "Error" , Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                });
    }

    // this method is used to save your data in firebase
    private static void saveUserAccount(UserModel users, String userID , Context con , ProgressDialog progressDialog) {
        UserModel userModel = new UserModel(users.getName() , users.getEmail() , "null") ;
        DATABASE_REFERENCE.child("Users").child(userID).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(con, MainActivity.class);
                con.startActivity(intent);
                ((Activity)con).finish();
                progressDialog.dismiss();
            }
        });
    }

    // this method is used to logout to your account and from app
    public static void singOut(Context context){
        AUTH.signOut();
        Intent intent = new Intent(context, SplashscreenActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
    // this method is used to login again
    public static void logIn(UserModel userModel , ProgressDialog progressDialog , Context context){
        AUTH.signInWithEmailAndPassword(userModel.getEmail() , userModel.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(context , MainActivity.class) ;
                            context.startActivity(intent);
                            ((Activity)context).finish();
                            progressDialog.dismiss();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(context , "Error" , Toast.LENGTH_SHORT).show();
                        }
                    }
                }) ;
    }
    // this mehod is used to check if you login or not
    public static void getCurrentUser(Context context){
        if (user != null){
            Intent intent = new Intent(context , MainActivity.class) ;
            context.startActivity(intent);
            ((Activity)context).finish();
        }
    }

    // this mehtod is used to handel if you forgot your password
    public static void forgotPassword(Context context , ProgressDialog progressDialog , String email){
        AUTH.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(context , "Please check your email now" , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context , LoginActivity.class) ;
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }else {
                    Toast.makeText(context , task.getException().getMessage() , Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    // this method is used to update prpduct info
    public static void updateData(String key , HashMap<String , Object> hashMap , Context context){
        DATABASE_REFERENCE.child("Products").child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context , "Successfully updated" , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context , MainActivity.class) ;
                    context.startActivity(intent);
                }
            }
        });
    }
    // this method is used to update prpduct info
    public static void anotheruApdateData(String key , HashMap<String , Object> hashMap , Context context){
        DATABASE_REFERENCE.child("Products").child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context , "Successfully updated" , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context , MainActivity.class) ;
                    context.startActivity(intent);
                    ((MainActivity)context).finish();
                }
            }
        });
    }
    /*

     */
    // this method is used to increase quantity from cart
    public static void increaseCart(String key , CartModel cartModel, Context context){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance() ;
        DatabaseReference reference = firebaseDatabase.getReference().child("Cart") ;
        DatabaseReference reference2 = firebaseDatabase.getReference().child("Products") ;
        int realQuantity = cartModel.getQuanCount() ;
        if (realQuantity != 0){
            int result = realQuantity - 1 ;
            HashMap<String , Object> hashMap = new HashMap<>() ;
            hashMap.put("id" , cartModel.getID()) ;
            hashMap.put("price" , cartModel.getPrice()) ;
            hashMap.put("name" , cartModel.getName()) ;
            hashMap.put("image" , cartModel.getImage()) ;
            hashMap.put("quanCount" , result) ;
            hashMap.put("quantity" , cartModel.getQuantity()) ;
            reference.child(user.getUid()).child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(context , "Successfully updated" , Toast.LENGTH_LONG).show();
                    }
                }
            });
            reference2.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    HashMap<String , Object> hashMap1 = new HashMap<>() ;
                    ItemModel itemModel = dataSnapshot.getValue(ItemModel.class);
                    hashMap1.put("category" , itemModel.getCategory()) ;
                    hashMap1.put("description" , itemModel.getDescription() ) ;
                    hashMap1.put("discount" , itemModel.getDiscount()) ;
                    hashMap1.put("favorite" , itemModel.getFavorite()) ;
                    hashMap1.put("id" , itemModel.getId()) ;
                    hashMap1.put("name" , itemModel.getName()) ;
                    hashMap1.put("photo" , itemModel.getPhoto()) ;
                    hashMap1.put("price" , itemModel.getPrice()) ;
                    hashMap1.put("quantity" , result) ;
                    hashMap1.put("rating" , itemModel.getRating()) ;
                    FirebaseOperations.anotheruApdateData(key , hashMap1 , context);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            Toast.makeText(context , "Item is finished so you can not add another qauntity", Toast.LENGTH_LONG).show();
        }
    }

    // this method is used to decrease quantity from cart
    public static void decreaseCart(String key , CartModel cartModel , int result , Context context){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance() ;
        DatabaseReference reference = firebaseDatabase.getReference().child("Cart") ;
        DatabaseReference reference2 = firebaseDatabase.getReference().child("Products") ;
        if (result != 1){
            int realQuantity = cartModel.getQuanCount() + 1 ;
            HashMap<String , Object> hashMap = new HashMap<>() ;
            hashMap.put("id" , cartModel.getID()) ;
            hashMap.put("price" , cartModel.getPrice()) ;
            hashMap.put("name" , cartModel.getName()) ;
            hashMap.put("image" , cartModel.getImage()) ;
            hashMap.put("quanCount" , realQuantity) ;
            hashMap.put("quantity" , cartModel.getQuantity()) ;
            reference.child(user.getUid()).child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(context , "Successfully updated" , Toast.LENGTH_LONG).show();
                    }
                }
            });
            reference2.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    HashMap<String , Object> hashMap1 = new HashMap<>() ;
                    ItemModel itemModel = dataSnapshot.getValue(ItemModel.class);
                    hashMap1.put("category" , itemModel.getCategory()) ;
                    hashMap1.put("description" , itemModel.getDescription() ) ;
                    hashMap1.put("discount" , itemModel.getDiscount()) ;
                    hashMap1.put("favorite" , itemModel.getFavorite()) ;
                    hashMap1.put("id" , itemModel.getId()) ;
                    hashMap1.put("name" , itemModel.getName()) ;
                    hashMap1.put("photo" , itemModel.getPhoto()) ;
                    hashMap1.put("price" , itemModel.getPrice()) ;
                    hashMap1.put("quantity" , realQuantity) ;
                    hashMap1.put("rating" , itemModel.getRating()) ;
                    FirebaseOperations.anotheruApdateData(key , hashMap1 , context);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            Toast.makeText(context , "you can not decrease " , Toast.LENGTH_LONG).show();
        }

    }

    // this method is used to update some information about user
    public static void updateUserInfo(String name , String email , Uri imageUri , ProgressDialog progressDialog ,Context context){
        UploadTask uploadTask;
        FirebaseStorage fbs = FirebaseStorage.getInstance();
        StorageReference storageReference = fbs.getReference().child("userImageUrl");
        final StorageReference rfs = storageReference.child(imageUri.getPath());
        uploadTask = rfs.putFile(imageUri);
        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Toast.makeText(context , task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
                return rfs.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri retrieveUriImage = task.getResult();
                String converImage = retrieveUriImage.toString();
                saveInfo(name , email , converImage , progressDialog ,context);
            }
        });
    }

    // this method is used to save information about user
    private static void saveInfo(String name, String email, String converImage, ProgressDialog progressDialog ,Context context) {
        UserModel userModel = new UserModel(name , email , converImage) ;
        DATABASE_REFERENCE.child("Users").child(user.getUid()).setValue(userModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context , "Successfully updated" , Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context , MainActivity.class) ;
                            context.startActivity(intent);
                            ((Activity)context).finish();
                            progressDialog.dismiss();
                        }
                    }
                });

    }
}
