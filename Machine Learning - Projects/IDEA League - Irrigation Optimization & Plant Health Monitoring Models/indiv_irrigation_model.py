from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_squared_error

def train_irrigation_model(df_combined, rand_seed=42):
    # Define features and target variable
    X = df_combined[['soil_moisture', 'soil_temperature', 'soil_pH', 'nutrient_levels', 'soil_salinity', 'ETc']]
    y = df_combined['optimal_irrigation']

    # Split the data into training and testing sets
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=rand_seed)

    # Train a Random Forest Regressor
    model = RandomForestRegressor(n_estimators=100, random_state=rand_seed)
    model.fit(X_train, y_train)

    # Make predictions
    y_pred = model.predict(X_test)

    # Evaluate the model
    mse = mean_squared_error(y_test, y_pred)
    print(f'Mean Squared Error: {mse}')

    return model, y_pred
