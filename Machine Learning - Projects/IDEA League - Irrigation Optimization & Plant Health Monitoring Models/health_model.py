from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_squared_error, r2_score
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

features = ['soil_moisture', 'soil_temperature', 'soil_pH', 'nutrient_levels', 'soil_salinity']
targets = ['plant_height', 'survival_rate', 'trunk_girth']

def train_health_model(df):
    # Split the data into training and testing sets
    X = df[features]
    y = df[targets]
    
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
    
    # Train a Random Forest Regressor for each target
    models = {}
    feature_importances = {}
    
    for target in targets:
        model = RandomForestRegressor(n_estimators=100, random_state=42)
        model.fit(X_train, y_train[target])
        models[target] = model
        
        # Make predictions
        y_pred = model.predict(X_test)
        
        # Evaluate the model
        mse = mean_squared_error(y_test[target], y_pred)
        r2 = r2_score(y_test[target], y_pred)
        print(f'{target} - Mean Squared Error: {mse}, R-squared: {r2}')
        
        # Calculate feature importances
        feature_importances[target] = model.feature_importances_
    
    feature_importances_df = pd.DataFrame(feature_importances, index=features)
    
    return models, feature_importances_df

def plot_feature_importances(feature_importances_df):
    # Melt the DataFrame for easier plotting with seaborn
    melted_df = feature_importances_df.reset_index().melt(id_vars='index')
    melted_df.columns = ['Feature', 'Target', 'Importance']
    
    # Create the bar plot
    plt.figure(figsize=(10, 6))
    sns.barplot(data=melted_df, x='Importance', y='Feature', hue='Target')
    plt.title('Feature Importances for Plant Health Targets')
    plt.xlabel('Importance')
    plt.ylabel('Feature')
    plt.legend(title='Target')
    plt.tight_layout()
    plt.show()

def correlation_analysis(df):
    df_combined = df[features + targets]

    # Compute correlation matrix
    correlation_matrix = df_combined.corr()

    # Plot the correlation matrix
    plt.figure(figsize=(12, 8))
    sns.heatmap(correlation_matrix, annot=True, cmap='coolwarm', center=0)
    plt.title('Correlation Matrix between Soil Conditions and Plant Health Metrics')
    plt.show()

