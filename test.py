from __future__ import print_function, division

import torch
import torch.nn as nn
import torch.optim as optim
from torch.optim import lr_scheduler
from torch.autograd import Variable
import numpy as np
import torchvision
from torchvision import datasets, models, transforms
import matplotlib.pyplot as plt
import time
import os
import copy
import pymysql


use_gpu = True
data_transforms = transforms.Compose([
        transforms.RandomSizedCrop(224),
        transforms.RandomHorizontalFlip(),
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
    ])
test_dir='test1'

conn = pymysql.connect(user='root',passwd='',db='DATA',host='localhost',port=3306)
x = conn.cursor()

data_transforms1 = {
    'train': transforms.Compose([
        transforms.RandomSizedCrop(224),
        transforms.RandomHorizontalFlip(),
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
    ]),
    'val': transforms.Compose([
        transforms.Scale(256),
        transforms.CenterCrop(224),
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
    ]),
}

data_dir = 'hymenoptera_data'
image_datasets1 = {x: datasets.ImageFolder(os.path.join(data_dir, x),
                                          data_transforms1[x])
                  for x in ['train', 'val']}
class_names = image_datasets1['train'].classes

image_datasets = datasets.ImageFolder(test_dir, data_transforms)
dataloader = torch.utils.data.DataLoader(image_datasets, batch_size=4, shuffle=True, num_workers=4)
def imshow(inp, title=None):
    """Imshow for Tensor."""
    inp = inp.numpy().transpose((1, 2, 0))
    mean = np.array([0.485, 0.456, 0.406])
    std = np.array([0.229, 0.224, 0.225])
    inp = std * inp + mean
    inp = np.clip(inp, 0, 1)
    plt.imshow(inp)
    if title is not None:
        plt.title(title)
    plt.pause(0.001)  # pause a bit so that plots are updated

def visualize_model(model, num_images=1):
    was_training = model.training
    model.eval()
    images_so_far = 0
    fig = plt.figure()

    for i, data in enumerate(dataloader): #dataloader_test
        inputs, labels = data
        if use_gpu:
            inputs, labels = Variable(inputs.cuda()), Variable(labels.cuda())
        else:
            inputs, labels = Variable(inputs), Variable(labels)

        outputs = model(inputs)
        _, preds = torch.max(outputs.data, 1)

        for j in range(inputs.size()[0]):
            images_so_far += 1
            ax = plt.subplot(num_images//1, 1, images_so_far)
            ax.axis('off')
            ax.set_title('The given pic is of {}'.format(class_names[preds[j][0]]))
            imshow(inputs.cpu().data[j],None)
	    try:
		 file = open("/home/aditya/Documents/JAIPUR/Wiki/%s.txt" % class_names[preds[j][0]], "r")
		 line = file.read()
		 x.execute("""TRUNCATE TABLE TRIAL""")
  		 x.execute("""INSERT INTO TRIAL VALUES (%s,%s)""",(class_names[preds[j][0]],line))
  		 conn.commit()
	    except:
 		  conn.rollback()

	    conn.close()

            if images_so_far == num_images:
                model.train(mode=was_training)
                return
    model.train(mode=was_training)

model = torch.load("model_ft.pt")
visualize_model(model)
plt.ioff()
plt.show()

while True:
    plt.pause(0.05)

