-- AI面试练习平台 - 数据库初始化脚本
-- 版本: 1.0.0
-- 日期: 2026-04-03

-- 创建数据库
CREATE DATABASE interview_ai WITH ENCODING 'UTF8';
\c interview_ai;

-- 1. 用户表 (users)
CREATE TABLE IF NOT EXISTS users (
    id              BIGSERIAL PRIMARY KEY,
    phone           VARCHAR(20) UNIQUE COMMENT '手机号',
    password_hash   VARCHAR(255) COMMENT '密码hash',
    nickname        VARCHAR(50) COMMENT '昵称',
    avatar_url      VARCHAR(500) COMMENT '头像URL',
    member_level    SMALLINT DEFAULT 0 COMMENT '会员等级:0免费,1Pro,2尊享',
    member_expire   TIMESTAMP COMMENT '会员到期时间',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP COMMENT '软删除时间',
    INDEX idx_phone (phone),
    INDEX idx_created (created_at)
);

-- 2. 简历表 (resumes)
CREATE TABLE IF NOT EXISTS resumes (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL COMMENT '用户ID',
    title           VARCHAR(100) COMMENT '简历标题',
    is_default      SMALLINT DEFAULT 0 COMMENT '是否默认简历',
    file_url        VARCHAR(500) COMMENT '简历文件URL(PDF/Word)',
    parse_status    SMALLINT DEFAULT 0 COMMENT '解析状态:0未解析,1解析中,2已解析',
    parse_result    JSON COMMENT 'AI解析结果',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP COMMENT '软删除时间',
    INDEX idx_user (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 3. 简历详情表 (resume_details)
CREATE TABLE IF NOT EXISTS resume_details (
    id              BIGSERIAL PRIMARY KEY,
    resume_id       BIGINT NOT NULL COMMENT '简历ID',
    section_type    VARCHAR(20) COMMENT '板块类型:basic,education,experience,skill',
    content         JSON COMMENT '板块内容',
    sort_order      INT DEFAULT 0 COMMENT '排序',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_resume (resume_id),
    FOREIGN KEY (resume_id) REFERENCES resumes(id)
);

-- 4. 面试记录表 (interviews)
CREATE TABLE IF NOT EXISTS interviews (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL COMMENT '用户ID',
    resume_id       BIGINT COMMENT '使用的简历ID',
    job_position    VARCHAR(50) COMMENT '目标岗位',
    interview_style VARCHAR(20) COMMENT '面试风格:technical,pressure',
    interaction_mode VARCHAR(20) COMMENT '交互模式:text,voice,video',
    status          VARCHAR(20) DEFAULT 'pending' COMMENT '状态:pending,running,completed,cancelled',
    start_time      TIMESTAMP COMMENT '开始时间',
    end_time        TIMESTAMP COMMENT '结束时间',
    duration        INT COMMENT '面试时长(秒)',
    total_score     DECIMAL(5,2) COMMENT '综合评分',
    score_details   JSON COMMENT '多维度评分详情',
    summary         TEXT COMMENT '面试总结',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_status (status),
    INDEX idx_created (created_at),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (resume_id) REFERENCES resumes(id)
);

-- 5. 面试问答表 (interview_qas)
CREATE TABLE IF NOT EXISTS interview_qas (
    id              BIGSERIAL PRIMARY KEY,
    interview_id    BIGINT NOT NULL COMMENT '面试ID',
    question_no     INT COMMENT '问题序号',
    question        TEXT COMMENT '面试问题',
    question_type   VARCHAR(20) COMMENT '问题类型:basic,practical,thinking,open',
    answer          TEXT COMMENT '用户回答',
    follow_count    INT DEFAULT 0 COMMENT '追问次数',
    answer_score    DECIMAL(5,2) COMMENT '回答评分',
    answer_feedback TEXT COMMENT '回答反馈',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_interview (interview_id),
    FOREIGN KEY (interview_id) REFERENCES interviews(id)
);

-- 6. 题库表 (questions)
CREATE TABLE IF NOT EXISTS questions (
    id              BIGSERIAL PRIMARY KEY,
    job_position    VARCHAR(50) COMMENT '岗位:frontend,backend,fullstack',
    category        VARCHAR(30) COMMENT '类别:html,css,js,react,vue,java,python,go,db...',
    difficulty      SMALLINT COMMENT '难度:1简单,2中等,3困难',
    content         TEXT COMMENT '问题内容',
    keywords        JSON COMMENT '考察关键词',
    standard_answer TEXT COMMENT '标准答案要点',
    follow_questions TEXT COMMENT '可能的追问问题',
    usage_count     INT DEFAULT 0 COMMENT '使用次数',
    correct_rate    DECIMAL(5,2) COMMENT '正确率',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_position (job_position),
    INDEX idx_category (category),
    INDEX idx_difficulty (difficulty)
);

-- 7. 会员订单表 (member_orders)
CREATE TABLE IF NOT EXISTS member_orders (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL COMMENT '用户ID',
    order_no        VARCHAR(64) UNIQUE COMMENT '订单号',
    member_level    SMALLINT COMMENT '购买的会员等级',
    duration_type   VARCHAR(20) COMMENT '时长类型:month,year',
    amount          DECIMAL(10,2) COMMENT '金额',
    status          VARCHAR(20) DEFAULT 'pending' COMMENT '状态:pending,paid,cancelled,refunded',
    paid_at         TIMESTAMP COMMENT '支付时间',
    expire_at       TIMESTAMP COMMENT '到期时间',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_order_no (order_no),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 8. 用户收藏/错题表 (user_collections)
CREATE TABLE IF NOT EXISTS user_collections (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL COMMENT '用户ID',
    question_id     BIGINT COMMENT '收藏的题目ID',
    type            VARCHAR(20) COMMENT '类型:collect,wrong',
    interview_id    BIGINT COMMENT '关联的面试记录ID',
    note            TEXT COMMENT '用户笔记',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_question (user_id, question_id),
    INDEX idx_type (type),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (question_id) REFERENCES questions(id),
    FOREIGN KEY (interview_id) REFERENCES interviews(id)
);

-- 注释说明
COMMENT ON TABLE users IS '用户表';
COMMENT ON TABLE resumes IS '简历表';
COMMENT ON TABLE resume_details IS '简历详情表';
COMMENT ON TABLE interviews IS '面试记录表';
COMMENT ON TABLE interview_qas IS '面试问答表';
COMMENT ON TABLE questions IS '题库表';
COMMENT ON TABLE member_orders IS '会员订单表';
COMMENT ON TABLE user_collections IS '用户收藏/错题表';
