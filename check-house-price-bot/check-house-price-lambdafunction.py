import boto3
import json
def lambda_handler(event, context):
    # get expected price
    s3client = boto3.client('s3')
    bucketName = 'generic-use'
    priceObjectName = 'check-house-price-bot/price.txt'
    with open('/tmp/price.txt', 'wb') as f:
        s3client.download_fileobj(bucketName, priceObjectName, f)
    with open('/tmp/price.txt', 'r') as f:
        price = int(f.read())
    # get price from website
    
    # sent notification if different
    
    return {
        'statusCode': 200,
        'body': json.dumps(price)
    }

comment='''
These are the special setup needed to support the Lambda function
    created CloudWatch rule trigger
    gave Lambda function permission to read/write S3
    set up price file in S3
'''

