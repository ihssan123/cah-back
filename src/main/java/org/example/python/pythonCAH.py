import sys
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from scipy.cluster.hierarchy import dendrogram, linkage
from scipy.spatial.distance import squareform
import Levenshtein
import uuid
def process_data(path):
    # Read string data from Excel file
    data = pd.read_excel(path)
    
    # Convert DataFrame to a list of strings
    data = data.iloc[:, 0].astype(str).tolist()

    # Calculate distance matrix using Levenshtein distance
    dist_matrix = np.zeros((len(data), len(data)))
    for i in range(len(data)):
        for j in range(i, len(data)):
            dist_matrix[i, j] = Levenshtein.distance(data[i], data[j])
            dist_matrix[j, i] = dist_matrix[i, j]  # Make the matrix symmetric

    # Convert the distance matrix to condensed form
    dist_condensed = squareform(dist_matrix)

    # Perform hierarchical clustering
    Z = linkage(dist_condensed, method='single')

    # Display the dendrogram with original class names as labels
    plt.figure(figsize=(10, 5))
    dendrogram(Z, labels=data)
    # Save the dendrogram as an image
    image_name = str(uuid.uuid4()) + ".png"
    plt.savefig("C:/Users/youss/Desktop/backCAH/src/main/webapp/WEB-INF/images/"+image_name)
    plt.close()
    return image_name

def main():
    function_name = sys.argv[1]
    additional_args = sys.argv[2:]

    if function_name == "process_data":
        result = process_data(*additional_args)
        print(result)
    else:
        print("Invalid function name")

if __name__ == "__main__":
    main()
