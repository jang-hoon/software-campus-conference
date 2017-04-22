import tensorflow as tf

# 학습 데이터
x_data = [1, 2, 3]
y_data = [1, 2, 3]

W = tf.Variable(tf.random_uniform([1], -1.0, 1.0))
b = tf.Variable(tf.random_uniform([1], -1.0, 1.0))
X = tf.placeholder(tf.float32)
Y = tf.placeholder(tf.float32)

# 가설 Y_ = WX + b
hypothesis = W * X + b

# 비용함수 cost = 평균( (Y_ - Y)^2 )
cost = tf.reduce_mean(tf.square(hypothesis - Y))

# cost를 줄이는 방향으로 W, b를 수정하기 위한 경사하강법
a = tf.Variable(0.1) # Learning rate, alpha
optimizer = tf.train.GradientDescentOptimizer(a)
train = optimizer.minimize(cost)

init = tf.global_variables_initializer()

sess = tf.Session()
sess.run(init)

# 학습
for step in range(2001):
    # hypothesis = W * X + b
    # cost = tf.reduce_mean(tf.square(hypothesis - Y))
    # train =  tf.train.GradientDescentOptimizer(0.1).minimize(cost)
    sess.run(train, feed_dict={X: x_data, Y: y_data})
    if step % 20 == 0:
        print(step, sess.run(cost, feed_dict={X: x_data, Y: y_data}), sess.run(W), sess.run(b))

# Hypothesis 테스트, hypothesis = W * X + b
print(sess.run(hypothesis, feed_dict={X: 5}))
print(sess.run(hypothesis, feed_dict={X: 2.5}))
