package com.example.ep_benjaminvillegasrojas_02122025;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProductActivity extends AppCompatActivity {

    private EditText productTitleEditText;
    private EditText productPriceEditText;
    private EditText productDescriptionEditText;
    private EditText productCategoryEditText;
    private EditText productImageEditText;
    private Button updateProductButton;
    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        productTitleEditText = findViewById(R.id.productTitleEditText);
        productPriceEditText = findViewById(R.id.productPriceEditText);
        productDescriptionEditText = findViewById(R.id.productDescriptionEditText);
        productCategoryEditText = findViewById(R.id.productCategoryEditText);
        productImageEditText = findViewById(R.id.productImageEditText);
        updateProductButton = findViewById(R.id.updateProductButton);

        productId = getIntent().getIntExtra("productId", -1);
        productTitleEditText.setText(getIntent().getStringExtra("productTitle"));
        productPriceEditText.setText(String.valueOf(getIntent().getDoubleExtra("productPrice", 0.0)));
        productDescriptionEditText.setText(getIntent().getStringExtra("productDescription"));
        productCategoryEditText.setText(getIntent().getStringExtra("productCategory"));
        productImageEditText.setText(getIntent().getStringExtra("productImage"));

        updateProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });
    }

    private void updateProduct() {
        String title = productTitleEditText.getText().toString().trim();
        String priceStr = productPriceEditText.getText().toString().trim();
        String description = productDescriptionEditText.getText().toString().trim();
        String category = productCategoryEditText.getText().toString().trim();
        String image = productImageEditText.getText().toString().trim();

        if (title.isEmpty() || priceStr.isEmpty() || description.isEmpty() || category.isEmpty() || image.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);

        Product product = new Product(title, price, description, category, image);

        FakeStoreApiService apiService = ApiClient.getClient().create(FakeStoreApiService.class);
        Call<Product> call = apiService.updateProduct(productId, product);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UpdateProductActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateProductActivity.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(UpdateProductActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
