package com.example.ep_benjaminvillegasrojas_02122025;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView productsRecyclerView;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        registerForContextMenu(productsRecyclerView);

        getProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProducts();
    }

    private void getProducts() {
        FakeStoreApiService apiService = ApiClient.getClient().create(FakeStoreApiService.class);
        Call<List<Product>> call = apiService.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productAdapter = new ProductAdapter(MainActivity.this, response.body());
                    productsRecyclerView.setAdapter(productAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                Log.e("MainActivity", "Error fetching products", t);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_product) {
            Intent intent = new Intent(this, AddProductActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = productAdapter.getPosition();
        Product selectedProduct = productAdapter.getProductAt(position);

        int itemId = item.getItemId();
        if (itemId == R.id.update_product) {
            Intent intent = new Intent(this, UpdateProductActivity.class);
            intent.putExtra("productId", selectedProduct.getId());
            intent.putExtra("productTitle", selectedProduct.getTitle());
            intent.putExtra("productPrice", selectedProduct.getPrice());
            intent.putExtra("productDescription", selectedProduct.getDescription());
            intent.putExtra("productCategory", selectedProduct.getCategory());
            intent.putExtra("productImage", selectedProduct.getImage());
            startActivity(intent);
            return true;
        } else if (itemId == R.id.delete_product) {
            deleteProduct(selectedProduct.getId());
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    private void deleteProduct(int productId) {
        FakeStoreApiService apiService = ApiClient.getClient().create(FakeStoreApiService.class);
        Call<Void> call = apiService.deleteProduct(productId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                    getProducts(); // Refresh the list
                } else {
                    Toast.makeText(MainActivity.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
