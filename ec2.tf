terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

data "aws_ami" "amazon_linux" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-*-x86_64-gp2"]
  }
}

resource "aws_security_group" "web_server" {
  name        = "terraform-maven-docker-sg"
  description = "Security group for Maven/Docker web server"

  # SSH
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # HTTP ingress
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 9000
    to_port     = 9000
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # HTTPS access
  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # All outbound traffic
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "terraform-maven-docker-sg"
  }
}

locals {
  user_data = <<-EOF
    #!/bin/bash
    yum update -y
    yum install -y docker
    systemctl start docker
    systemctl enable docker
    usermod -a -G docker ec2-user
    mkdir -p /opt/app
    chown ec2-user:ec2-user /opt/app
    # Apply group changes (important for CI/CD)
    newgrp docker
  EOF
}

# Create EC2 Instance
resource "aws_instance" "web_server" {
  ami                         = data.aws_ami.amazon_linux.id
  instance_type              = var.instance_type
  key_name                   = var.key_name
  vpc_security_group_ids     = [aws_security_group.web_server.id]
  associate_public_ip_address = true
  
  user_data = base64encode(local.user_data)

  tags = {
    Name        = "terraform-maven-docker-server"
    Environment = "development"
    Purpose     = "maven-docker-cicd"
    Application = "java-maven-app"
  }
}