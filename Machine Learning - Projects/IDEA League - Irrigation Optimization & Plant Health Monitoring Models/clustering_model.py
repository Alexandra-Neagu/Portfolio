import pandas as pd
import matplotlib.pyplot as plt
from sklearn.preprocessing import StandardScaler
from sklearn.cluster import KMeans
from sklearn.metrics import silhouette_score
from kneed import KneeLocator
import seaborn as sns

def perform_clustering(df_combined, features, rand_seed=42):
    # Standardize the features
    scaler = StandardScaler()
    X_scaled = scaler.fit_transform(df_combined[features])

    # Determine the optimal number of clusters using the Elbow method
    wcss = []
    for i in range(1, 11):
        kmeans = KMeans(n_clusters=i, random_state=rand_seed)
        kmeans.fit(X_scaled)
        wcss.append(kmeans.inertia_)

    # Find the elbow point using the Kneedle algorithm
    kneedle = KneeLocator(range(1, 11), wcss, curve='convex', direction='decreasing')
    optimal_clusters = kneedle.elbow

    # Plot the Elbow method graph
    plt.plot(range(1, 11), wcss, marker='o')
    plt.axvline(x=optimal_clusters, color='r', linestyle='--', label=f'Optimal clusters: {optimal_clusters}')
    plt.title('Elbow Method')
    plt.xlabel('Number of clusters')
    plt.ylabel('WCSS')
    plt.legend()
    plt.show()

    # Apply KMeans with optimal clusters
    kmeans = KMeans(n_clusters=optimal_clusters, random_state=rand_seed)
    df_combined['cluster'] = kmeans.fit_predict(X_scaled)

    # Analyze clusters
    cluster_centers = scaler.inverse_transform(kmeans.cluster_centers_)
    cluster_data = pd.DataFrame(cluster_centers, columns=features)
    
    # Define a basic strategy for each cluster
    irrigation_strategies = []
    cluster_counts = []
    for i in range(optimal_clusters):
        cluster_mean_irrigation = df_combined[df_combined['cluster'] == i]['optimal_irrigation'].mean()
        irrigation_strategies.append(cluster_mean_irrigation)
        cluster_count = df_combined[df_combined['cluster'] == i].shape[0]
        cluster_counts.append(cluster_count)

    # Print irrigation strategies and number of plants in each cluster
    print(f"Irrigation Strategies (mm/day) for each cluster: {irrigation_strategies}")
    print(f"Number of plants in each cluster: {cluster_counts}")

    # Stats
    irrigation_stats = []
    for i in range(optimal_clusters):
        cluster_data = df_combined[df_combined['cluster'] == i]['optimal_irrigation']
        stats = {
            'mean': cluster_data.mean(),
            'median': cluster_data.median(),
            'std_dev': cluster_data.std(),
            'min': cluster_data.min(),
            'max': cluster_data.max(),
            'count': cluster_data.shape[0]
        }
        irrigation_stats.append(stats)

    for i, stats in enumerate(irrigation_stats):
        print(f"Cluster {i}: {stats}")

    feature_stats = {}
    for feature in features:
        feature_stats[feature] = []
        for i in range(optimal_clusters):
            cluster_data = df_combined[df_combined['cluster'] == i][feature]
            stats = {
                'mean': cluster_data.mean(),
                'median': cluster_data.median(),
                'std_dev': cluster_data.std(),
                'min': cluster_data.min(),
                'max': cluster_data.max()
            }
            feature_stats[feature].append(stats)

    for feature, stats_list in feature_stats.items():
        print(f"\nFeature: {feature}")
        for i, stats in enumerate(stats_list):
            print(f"Cluster {i}: {stats}")

    # Visualizations
    sns.pairplot(df_combined, hue='cluster', palette='Set1', vars=features)
    plt.show()

    plt.bar(range(optimal_clusters), cluster_counts)
    plt.xlabel('Cluster')
    plt.ylabel('Number of Plants')
    plt.title('Number of Plants in Each Cluster')
    plt.show()

    silhouette_avg = silhouette_score(X_scaled, df_combined['cluster'])
    print(f"Silhouette Score: {silhouette_avg}")
